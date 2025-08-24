package com.example.goinangshopping.repository;

import com.example.goinangshopping.model.Item;
import com.example.goinangshopping.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByCategoryAndAvailable(ItemCategory category, boolean available);
}