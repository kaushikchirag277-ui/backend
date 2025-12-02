package com.example.Hello_world_api.controller;

import com.example.Hello_world_api.model.Store;
import com.example.Hello_world_api.model.UserLogin;
import com.example.Hello_world_api.repository.StoreRepository;
import com.example.Hello_world_api.repository.UserLoginRepository;
import com.example.Hello_world_api.service.OtpService;
import com.example.Hello_world_api.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private StoreRepository storeRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String mobile = request.get("mobile");
            if (mobile == null || mobile.isEmpty()) {
                return ResponseEntity.badRequest().body("Mobile number is required");
            }

            UserLogin user = userLoginRepository.findByMobile(mobile);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile number not registered");
            }

            otpService.generateOtp(mobile);
            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String mobile = request.get("mobile");
        String otp = request.get("otp");

        if (mobile == null || otp == null) {
            return ResponseEntity.badRequest().body("Mobile and OTP are required");
        }

        if (!otpService.verifyOtp(mobile, otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }

        UserLogin user = userLoginRepository.findByMobile(mobile);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generate JWT token
        String token = JwtUtil.generateToken(user.getId(), user.getRole());

        // Fetch assigned stores
        List<String> shopIds = user.getShops();
        List<Store> stores = storeRepository.findStoreNamesByIds(shopIds);
        List<Map<String, String>> shopList = stores.stream()
                .map(store -> Map.of("id", store.getId(), "name", store.getName()))
                .collect(Collectors.toList());

        // Prepare user response
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("name", user.getName());
        userResponse.put("mobile", user.getMobile());
        userResponse.put("role", user.getRole());
        userResponse.put("shops", shopList);

        otpService.clearOtp(mobile);

        // Final response with JWT
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", token);
        response.put("user", userResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("Authorization token is missing or invalid");
    }

    String token = authHeader.substring(7); // Remove "Bearer " prefix


    return ResponseEntity.ok(Map.of(
        "status", "success",
        "message", "Logged out successfully"
    ));
}
}
