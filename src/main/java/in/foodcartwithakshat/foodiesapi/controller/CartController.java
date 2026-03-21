package in.foodcartwithakshat.foodiesapi.controller;


import in.foodcartwithakshat.foodiesapi.io.CartRequest;
import in.foodcartwithakshat.foodiesapi.io.CartResponse;
import in.foodcartwithakshat.foodiesapi.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        String foodId = request.getFoodId();

        if(foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId not found");
        }
        return cartService.addToCart(request);

    }

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
        cartService.clearAllCart();
    }

    @PostMapping("/remove")
    public CartResponse removeItemFromCart(@RequestBody CartRequest cartRequest){
        String foodId = cartRequest.getFoodId();

        if(foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foodId not found");
        }

        return cartService.removeItemFromCart(cartRequest);
    }


}
