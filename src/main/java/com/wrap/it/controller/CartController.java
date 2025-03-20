package com.wrap.it.controller;

import com.wrap.it.dto.cart.CartDto;
import com.wrap.it.dto.item.cart.CartItemDto;
import com.wrap.it.dto.item.cart.CreateCartItemRequestDto;
import com.wrap.it.dto.item.cart.UpdateCartItemRequestDto;
import com.wrap.it.model.User;
import com.wrap.it.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

@Tag(name = "cart management", description = "Endpoints for management cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Validated
public class CartController {
    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get sopping cart", description = "Return user's cart")
    public CartDto getCart(Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        return cartService.getCart(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add item to cart", description = "Add item to user's cart")
    public CartItemDto addItemToCart(
            @RequestBody @Valid CreateCartItemRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.addItemToCart(requestDto, user);
    }

    @PutMapping("/{cartItemId}")
    @Operation(summary = "Change quantity of item",
            description = "Change quantity of item, selected by id")
    public CartItemDto updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDro,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.updateItemQuantity(cartItemId, requestDro, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "Remove item from cart",
            description = "Delete item, selected by id")
    public void deleteItemFromCart(@PathVariable Long cartItemId,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.deleteItemFromCart(cartItemId, user);
    }
}
