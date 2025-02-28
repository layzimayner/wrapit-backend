package com.wrap.it.controller;

import com.wrap.it.dto.item.ItemDto;
import com.wrap.it.dto.item.ItemRequest;
import com.wrap.it.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
    private ItemService itemService;

    @PostMapping
    @Operation(summary = "Create item", description = "Add new item to DB")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(ItemRequest request) {
        return itemService.create(request);
    }
}
