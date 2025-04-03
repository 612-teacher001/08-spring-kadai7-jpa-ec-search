package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Controller
public class ItemController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ItemRepository itemRepository;

	// 商品一覧表示
	@GetMapping("/items")
	public String index(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(name = "maxPrice", required = false) String maxPriceString, // TODO: ver.0.30での指摘を改修
			Model model) {

		// 全カテゴリー一覧を取得
		List<Category> categoryList = categoryRepository.findAll();
		model.addAttribute("categories", categoryList);
		
		// maxPriceパラメータの判定
		Integer maxPrice;
		if  (maxPriceString == null || maxPriceString.isEmpty()) {
			maxPrice = null;
		} else {
			maxPrice = Integer.valueOf(maxPriceString);
		}

		// 商品一覧情報の取得
		List<Item> itemList = null;
		if (categoryId == null) {
			itemList = itemRepository.findAll();
		} else {
			// itemsテーブルをカテゴリーIDを指定して一覧を取得
			itemList = itemRepository.findByCategoryId(categoryId);
		}
		
		// キーワード検索と価格検索
		if (!keyword.isEmpty()) {
			// キーワードが入力されている場合
			if (maxPrice != null && maxPrice > 0) {
				// 価格が入力されている場合
				itemList = itemRepository.findByNameContainsAndPriceLessThanEqual(keyword, maxPrice);
			} else {
				// 価格が入力されていない場合
				itemList = itemRepository.findByNameContains(keyword);
			}
		} else {
			// キーワードが入力されない場合
			if (maxPrice != null && maxPrice > 0) {
				// 価格が入力されている場合
				itemList = itemRepository.findByPriceLessThanEqual(maxPrice);
			}
		}
		
		model.addAttribute("items", itemList);
		model.addAttribute("keyword", keyword);
		model.addAttribute("maxPrice", maxPrice);

		return "items";
	}
}
