package com.example.goinangshopping.controller;

import com.example.goinangshopping.dto.request.ItemCreateDto;
import com.example.goinangshopping.dto.response.ItemResponseDto;
import com.example.goinangshopping.model.ItemCategory;
import com.example.goinangshopping.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // GET all items
    @GetMapping
    public List<ItemResponseDto> getAllItems() {
        return itemService.getAllItems();
    }

    // GET a single item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE a new item
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemCreateDto itemCreateDto) {
        ItemResponseDto createdItem = itemService.createItem(itemCreateDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    // UPDATE an existing item
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemCreateDto itemCreateDto) {
        return itemService.updateItem(id, itemCreateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE an item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.getItemById(id).isPresent()) {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // SEARCH items by name
    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByName(@RequestParam String name) {
        return itemService.searchItemsByName(name);
    }

    // GET items by category and availability
    @GetMapping("/category")
    public List<ItemResponseDto> getItemsByCategoryAndAvailability(
            @RequestParam ItemCategory category,
            @RequestParam(required = false, defaultValue = "true") boolean available) {
        return itemService.getItemsByCategoryAndAvailability(category, available);
    }
}