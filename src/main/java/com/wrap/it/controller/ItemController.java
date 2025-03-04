package com.wrap.it.controller;

import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemDtoWithoutCategoryIds;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "item management",
        description = "Endpoints for management items")
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "Create item", description = "Add new item to DB")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody @Valid ItemRequest request) {
        return itemService.save(request);
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Get a list of all available items")
    public Page<ItemDtoWithoutCategoryIds> getAllItems(Pageable pageable) {
        return itemService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "Delete item by id")
    public void deleteItem(@PathVariable @Positive Long id) {
        itemService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "Update item by id")
    public ItemDto updateItem(@PathVariable @Positive Long id,
                              @RequestBody @Valid ItemRequest requestDto) {
        return itemService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by id", description = "Get item by id")
    public ItemDto getItemById(@PathVariable @Positive Long id) {
        return itemService.findById(id);
    }
}
