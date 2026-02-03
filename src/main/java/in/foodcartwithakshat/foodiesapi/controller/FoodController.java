package in.foodcartwithakshat.foodiesapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.foodcartwithakshat.foodiesapi.io.FoodRequest;
import in.foodcartwithakshat.foodiesapi.io.FoodResponse;
import in.foodcartwithakshat.foodiesapi.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;

        try{
            request = objectMapper.readValue(foodString, FoodRequest.class);

        }catch(JsonProcessingException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }

        FoodResponse foodResponse = foodService.addFood(request,file);
        return foodResponse;

    }

    @GetMapping
    public List<FoodResponse> readFoods() {
        return foodService.readFoods();
    }

}
