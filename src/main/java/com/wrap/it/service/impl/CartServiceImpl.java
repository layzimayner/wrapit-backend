package com.wrap.it.service.impl;

import com.wrap.it.dto.cart.CartDto;
import com.wrap.it.dto.item.cart.CartItemDto;
import com.wrap.it.dto.item.cart.CreateCartItemRequestDto;
import com.wrap.it.dto.item.cart.UpdateCartItemRequestDto;
import com.wrap.it.exception.EntityNotFoundException;
import com.wrap.it.exception.TooLargeAmountException;
import com.wrap.it.mapper.CartMapper;
import com.wrap.it.model.CartItem;
import com.wrap.it.model.Item;
import com.wrap.it.model.ShoppingCart;
import com.wrap.it.model.User;
import com.wrap.it.repository.CartItemRepository;
import com.wrap.it.repository.ItemRepository;
import com.wrap.it.repository.ShoppingCartRepository;
import com.wrap.it.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartDto getCart(Long userId) {
        return cartRepository.getCartByUserId(userId)
                .map(cart -> {
                    CartDto cartDto = cartMapper.toDto(cart);
                    cartDto.setCartItems(cartMapper.mapCartItemsToDto(cart.getCartItems()));
                    return cartDto;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user with ID: " + userId));
    }

    @Transactional
    @Override
    public CartItemDto addItemToCart(CreateCartItemRequestDto requestDto, User user) {
        Item item = itemRepository.findByIdWithCategories(requestDto.getItemId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item not found with ID: " + requestDto.getItemId()));

        if (item.getQuantity() < requestDto.getQuantity()) {
            throw new TooLargeAmountException("We don't have " + requestDto.getQuantity()
                + " " + item.getName() + ", only " + item.getQuantity() + " are available");
        }

        item.setQuantity(item.getQuantity() - requestDto.getQuantity());
        itemRepository.save(item);

        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(i -> i.getItem().getId().equals(item.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setItem(item);
            cartItem.setQuantity(requestDto.getQuantity());
            shoppingCart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        }

        cartRepository.save(shoppingCart);
        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    @Override
    public CartItemDto updateItemQuantity(Long id,
                                          UpdateCartItemRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id + " in user's shopping cart"));

        Item item = cartItem.getItem();

        int newQuantity = item.getQuantity() + (cartItem.getQuantity() - requestDto.getQuantity());

        if (newQuantity < 0) {
            throw new TooLargeAmountException("We don't have " + requestDto.getQuantity()
                    + " " + item.getName() + ", only " + item.getQuantity() + " are available");
        }

        item.setQuantity(newQuantity);
        itemRepository.save(item);

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    @Override
    public void deleteItemFromCart(Long id, User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id + " in user's shopping cart"));

        Item item = cartItem.getItem();

        item.setQuantity(item.getQuantity() + cartItem.getQuantity());
        itemRepository.save(item);

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
