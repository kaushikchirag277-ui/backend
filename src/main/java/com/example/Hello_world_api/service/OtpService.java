package com.example.Hello_world_api.service;

import com.example.Hello_world_api.util.MongoUtil; // Adjust package if needed
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    private MongoCollection<Document> getUserLoginCollection() {
        return MongoUtil.getUserLoginCollection(); // Reuse your MongoUtil class
    }

    // Generate OTP and save it in MongoDB
    public String generateOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP

        Document filter = new Document("mobile", mobile);
        Document update = new Document("$set", new Document("otp", otp));
        UpdateResult result = getUserLoginCollection().updateOne(filter, update);
        System.out.println("Matched count: " + result.getMatchedCount());
        System.out.println("Modified count: " + result.getModifiedCount());
        //getCollection().updateOne(filter, update);

        return otp;
    }

    // Verify OTP from MongoDB
    public boolean verifyOtp(String mobile, String otp) {
        Document doc = getUserLoginCollection().find(new Document("mobile", mobile)).first();
        return doc != null && otp.equals(doc.getString("otp"));
    }

    // Clear OTP after successful verification
    public void clearOtp(String mobile) {
        Document filter = new Document("mobile", mobile);
        Document update = new Document("$unset", new Document("otp", ""));
        getUserLoginCollection().updateOne(filter, update);
    }
}

// package com.example.Hello_world_api.service;

// // public class OtpService {
    
// // }

// //package com.example.Hello_world_api.service;

// import org.springframework.stereotype.Service;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.Random;

// @Service
// public class OtpService {

//     private Map<String, String> otpStorage = new HashMap<>();

//     // Generate OTP and store it
//     public String generateOtp(String mobile) {
//         String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit
//         otpStorage.put(mobile, otp);
//         return otp;
//     }

//     // Verify OTP
//     public boolean verifyOtp(String mobile, String otp) {
//         return otp.equals(otpStorage.get(mobile));
//     }

//     // Clear OTP after successful verification
//     public void clearOtp(String mobile) {
//         otpStorage.remove(mobile);
//     }
// }
