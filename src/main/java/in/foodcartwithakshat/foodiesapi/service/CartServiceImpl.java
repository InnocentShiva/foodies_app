package in.foodcartwithakshat.foodiesapi.service;


import in.foodcartwithakshat.foodiesapi.entity.CartEntity;
import in.foodcartwithakshat.foodiesapi.io.CartRequest;
import in.foodcartwithakshat.foodiesapi.io.CartResponse;
import in.foodcartwithakshat.foodiesapi.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;

    private final UserRegistrationService userRegistrationService;

    @Override
    public CartResponse addToCart(CartRequest request) {
        String loggedInUserId = userRegistrationService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart =  cartOptional.orElseGet(() -> new CartEntity(loggedInUserId, new HashMap<>()));
        Map<String, Integer> cartItems = cart.getItems();
        cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0) + 1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId =  userRegistrationService.findByUserId();
        CartEntity entity = cartRepository.findByUserId(loggedInUserId).orElse(new CartEntity(null, loggedInUserId, new HashMap<>()));
        return convertToResponse(entity);
    }

    @Override
    public void clearAllCart() {
        String loggedInUserId =  userRegistrationService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeItemFromCart(CartRequest cartRequest) {
        String loggedInUserId =  userRegistrationService.findByUserId();
//        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
//        CartEntity cart =  cartOptional.orElseGet(() -> new CartEntity(loggedInUserId, new HashMap<>()));
//        Map<String, Integer> cartItems = cart.getItems();
//        cartItems.put(cartRequest.getFoodId(), cartItems.getOrDefault(cartRequest.getFoodId(), 0) - 1);
//        cart.setItems(cartItems);
//        cart = cartRepository.save(cart);
        CartEntity cartEntity = cartRepository.findByUserId(loggedInUserId).orElseThrow(() -> new RuntimeException("Cart is not found"));
        Map<String, Integer> cartItems = cartEntity.getItems();
        if(cartItems.containsKey(cartRequest.getFoodId())) {
            int currentQty = cartItems.get(cartRequest.getFoodId());
            if (currentQty > 0) {
                cartItems.put(cartRequest.getFoodId(), --currentQty);
                if(currentQty == 0) {
                    cartItems.remove(cartRequest.getFoodId());
                }
            } else {
                cartItems.remove(cartRequest.getFoodId());
            }
            cartEntity = cartRepository.save(cartEntity);
        }
        return convertToResponse(cartEntity);
    }

    private CartResponse convertToResponse(CartEntity cartEntity) {
        return CartResponse.builder()
                .id(cartEntity.getId())
                .userId(cartEntity.getUserId())
                .items(cartEntity.getItems())
                .build();
    }
}
