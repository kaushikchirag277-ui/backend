package com.example.Hello_world_api.controller;

import com.example.Hello_world_api.model.*;
import com.example.Hello_world_api.repository.*;
import com.example.Hello_world_api.service.OtpService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserLoginRepository userLoginRepository;

    public AuthController() {
        this.otpService = new OtpService(); // if you're using service logic here
        this.userLoginRepository = new UserLoginRepository();
    }

    @PostMapping("/send-otp")
public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
    try {
        String mobile = request.get("mobile");
        UserLogin user = userLoginRepository.findByMobile(mobile);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile number not registered");
        }
        String otp = otpService.generateOtp(mobile);
        return ResponseEntity.ok("OTP sent: " + otp);
    } catch (Exception e) {
        e.printStackTrace();  // See full error stack in console/log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Internal Server Error: " + e.getMessage());
    }
}
@PostMapping("/verify-otp")
public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
    String mobile = request.get("mobile");
    String otp = request.get("otp");

    if (otpService.verifyOtp(mobile, otp)) {
        UserLogin user = userLoginRepository.findByMobile(mobile);
        if (user != null) {
            List<String> shopIds = user.getShops(); // Or user.getAssignedStores() depending on your field name

            // Fetch store names from Store collection
            StoreRepository storeRepository = new StoreRepository(); // assuming you pass mongoDatabase
            List<Store> stores = storeRepository.findStoreNamesByIds(shopIds); // Store is your store model
            List<Map<String, String>> shopList = stores.stream()
                .map(store -> Map.of("id", store.getId(), "name", store.getName()))
                .collect(Collectors.toList());

            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("id", user.getId());
            userResponse.put("name", user.getName());
            userResponse.put("mobile", user.getMobile());
            userResponse.put("role", user.getRole());
            userResponse.put("shops", shopList);

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "user", userResponse
            ));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }
}
//     @PostMapping("/verify-otp")
//     public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
//     String mobile = request.get("mobile");
//     String otp = request.get("otp");

//     if (otpService.verifyOtp(mobile, otp)) {
//         //otpService.clearOtp(mobile);
//         UserLogin user = userLoginRepository.findByMobile(mobile); // Assuming this works
//         if (user != null) {
//             return ResponseEntity.ok(Map.of(
//                 "status", "success",
//                 "user", user
//             ));
//         } else {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//         }
//     } else {
//         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
//     }
// }
}

