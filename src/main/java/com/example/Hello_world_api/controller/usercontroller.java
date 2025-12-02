package com.example.Hello_world_api.controller;

import com.example.Hello_world_api.model.user;
import com.example.Hello_world_api.repository.userrepository;
import com.example.Hello_world_api.util.MongoUtil;
import com.mongodb.client.MongoDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import org.bson.types.ObjectId;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class usercontroller {

    private final userrepository userrepository;

    @Autowired
    public usercontroller(userrepository userrepository) {
        this.userrepository = userrepository;
    }

    // ✅ Create a new user with duplicate check
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody user user) {
        try {
            String result = userrepository.save(user);
            if (result.startsWith("Error")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Internal error: " + e.getMessage());
        }
    }

    // ✅ Get all users
    @GetMapping
    public List<user> getAllUsers() {
        return userrepository.getAllUsers();
    }

    // ✅ Get user by MongoDB ID
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        user user = userrepository.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("User not found with ID: " + id);
        }
    }

    @GetMapping("/test")
public String testConnection() {
    try {
        MongoDatabase db = MongoUtil.getDatabase();
        return "Connected to: " + db.getName();
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
}

    // ✅ Update full user document by ID (fixed)
    // @PutMapping("/{id}")
    // public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody user user) {
    //     try {
    //         boolean updated = userrepository.updateUserById(id, user);
    //         if (updated) {
    //             return ResponseEntity.ok("User updated successfully.");
    //         } else {
    //             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                                  .body("Duplicate userId found. Please use a unique one.");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                              .body("Error while updating user: " + e.getMessage());
    //     }
    // }

    @PutMapping("/{userId}")
public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody user user) {
    try {
        boolean updated = userrepository.updateUserByUserId(userId, user);
        if (updated) {
            return ResponseEntity.ok("User updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Duplicate userId found. Please use a unique one.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error while updating user: " + e.getMessage());
    }
}

    // ✅ Delete user by userId
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUserByUserId(@PathVariable String userId) {
        try {
            userrepository.deleteUserByUserId(userId); // Using repository method to delete by userId
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error while deleting user: " + e.getMessage());
        }
    }
}

// @RestController
// @RequestMapping("/api/users")
// public class usercontroller {

//     private final userrepository userrepository;

//     @Autowired
//     public usercontroller(userrepository userrepository) {
//         this.userrepository = userrepository;
//     }

//     // ✅ Create a new user with duplicate check
//     @PostMapping
//     public ResponseEntity<String> createUser(@RequestBody user user) {
//         try {
//             String result = userrepository.save(user);
//             if (result.startsWith("Error")) {
//                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
//             }
//             return ResponseEntity.status(HttpStatus.CREATED).body(result);
//         } catch (Exception e) {
//             e.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                  .body("Internal error: " + e.getMessage());
//         }
//     }

//     // ✅ Get all users
//     @GetMapping
//     public List<user> getAllUsers() {
//         return userrepository.getAllUsers();
//     }

//     // ✅ Get user by MongoDB ID
//     @GetMapping("/user/{id}")
//     public ResponseEntity<?> getUser(@PathVariable String id) {
//         user user = userrepository.getUserById(id);
//         if (user != null) {
//             return ResponseEntity.ok(user);
//         } else {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                  .body("User not found with ID: " + id);
//         }
//     }

//     // ✅ Update full user document by ID (fixed)
//      @PutMapping("/{id}")
//      public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody user user) {
//     try {
//         boolean updated = userrepository.updateUserById(id, user);
//         if (updated) {
//             return ResponseEntity.ok("User updated successfully.");
//         } else {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                                  .body("Duplicate userId found. Please use a unique one.");
//         }
//     } catch (Exception e) {
//         e.printStackTrace();
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                              .body("Error while updating user: " + e.getMessage());
//     }

   
// }
//     // ✅ Delete user by ID
//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> deleteUser(@PathVariable String id) {
//         try {
//             userrepository.deleteUserByUserId(id);
//             return ResponseEntity.ok("User deleted successfully.");
//         } catch (Exception e) {
//             e.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                  .body("Error while deleting user: " + e.getMessage());
//         }
//     }
// }
