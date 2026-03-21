package in.foodcartwithakshat.foodiesapi.service;

import in.foodcartwithakshat.foodiesapi.io.CartRequest;
import in.foodcartwithakshat.foodiesapi.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest foodId);

    CartResponse getCart();

    void clearAllCart();

    CartResponse removeItemFromCart(CartRequest cartRequest);

}
