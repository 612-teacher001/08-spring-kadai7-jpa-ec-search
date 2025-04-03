package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	// SELECT * FROM items WHERE category_id = ?
	List<Item> findByCategoryId(Integer categoryId);
	// SELECT * FROM items WHERE name LIKE ?
	List<Item> findByNameContains(String keyword);
	// SELECT * FROM items WHERE name LIKE :keyword AND price <= :maxPrice
	List<Item> findByNameContainsAndPriceLessThanEqual(String keyword, Integer maxPrice);
	// SELECT * FROM items WHERE price <= ?
	List<Item> findByPriceLessThanEqual(Integer maxPrice);
}
