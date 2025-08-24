package com.example.goinangshopping.service;

import com.example.goinangshopping.dto.request.ItemCreateDto;
import com.example.goinangshopping.dto.response.ItemResponseDto;
import com.example.goinangshopping.model.Item;
import com.example.goinangshopping.model.ItemCategory;
import com.example.goinangshopping.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Convert Entity to DTO
    public ItemResponseDto convertToDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setDiscount(item.getDiscount());
        dto.setImageUrl(item.getImageUrl());
        dto.setCategory(item.getCategory());
        dto.setAvailable(item.isAvailable());
        return dto;
    }

    // Lấy tất cả các món đồ uống
    public List<ItemResponseDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Lấy một món đồ uống theo ID
    public Optional<ItemResponseDto> getItemById(Long id) {
        return itemRepository.findById(id).map(this::convertToDto);
    }

    // Tạo một món đồ uống mới
    public ItemResponseDto createItem(ItemCreateDto itemCreateDto) {
        Item item = new Item();
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());
        item.setPrice(itemCreateDto.getPrice());
        item.setDiscount(itemCreateDto.getDiscount());
        item.setImageUrl(itemCreateDto.getImageUrl());
        item.setCategory(itemCreateDto.getCategory());
        item.setAvailable(itemCreateDto.isAvailable());

        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    // Cập nhật thông tin một món đồ uống
    public Optional<ItemResponseDto> updateItem(Long id, ItemCreateDto updatedItemDto) {
        return itemRepository.findById(id).map(existingItem -> {
            existingItem.setName(updatedItemDto.getName());
            existingItem.setDescription(updatedItemDto.getDescription());
            existingItem.setPrice(updatedItemDto.getPrice());
            existingItem.setDiscount(updatedItemDto.getDiscount());
            existingItem.setImageUrl(updatedItemDto.getImageUrl());
            existingItem.setCategory(updatedItemDto.getCategory());
            existingItem.setAvailable(updatedItemDto.isAvailable());
            Item savedItem = itemRepository.save(existingItem);
            return convertToDto(savedItem);
        });
    }

    // Xóa một món đồ uống
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Tìm các món đồ uống theo tên (tìm kiếm một phần)
    public List<ItemResponseDto> searchItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Tìm các món đồ uống theo danh mục và trạng thái sẵn có
    public List<ItemResponseDto> getItemsByCategoryAndAvailability(ItemCategory category, boolean available) {
        return itemRepository.findByCategoryAndAvailable(category, available).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}