
package com.example.Hello_world_api.repository;

import com.example.Hello_world_api.model.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class userrepository {
    private final MongoDatabase database;

    public userrepository() {
        this.database = MongoClients.create("mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net")
                                     .getDatabase("Kx");
    }

    private MongoCollection<Document> getCollection() {
        return database.getCollection("stores");
    }

    // ✅ Create new user only (inserts document)
    public String save(user user) {
        MongoCollection<Document> collection = getCollection();

        // Validate uniqueness
        if (user.getUserId() != null && existsByUserId(user.getUserId())) {
            return "UserId already exists!";
        }

        // Generate userId if not provided
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(generateNextUserId());
        }

        Document doc = new Document("userId", user.getUserId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("mobileNumber", user.getMobileNumber())
                .append("age", user.getAge())
                .append("gender", user.getGender());

        collection.insertOne(doc);
        return "User created with ID: " + user.getUserId();
    }

    // ✅ Get all users
    public List<user> getAllUsers() {
        MongoCollection<Document> collection = getCollection();
        List<user> users = new ArrayList<>();
        for (Document doc : collection.find()) {
            user user = new user();
            user.setId(doc.getObjectId("_id"));
            user.setUserId(doc.getString("userId"));
            user.setName(doc.getString("name"));
            user.setEmail(doc.getString("email"));
            user.setMobileNumber(doc.getString("mobileNumber"));
            user.setAge(doc.getInteger("age"));
            user.setGender(doc.getString("gender"));
            users.add(user);
        }
        return users;
    }

   public List<Store> getAllStores() {
    MongoCollection<Document> collection = getCollection();
    List<Store> stores = new ArrayList<>();

    for (Document doc : collection.find()) {
        Store store = new Store();
        store.setId(doc.getString("_id"));
        store.setName(doc.getString("name"));
       // store.setLocation(doc.getString("location"));

        stores.add(store);   // IMPORTANT: Add each store to the list
    }

    return stores;  // Return the final list
}

    

    // ✅ Get user by ID
    public user getUserById(String id) {
        MongoCollection<Document> collection = getCollection();
        Document doc;

        try {
            doc = collection.find(new Document("_id", new ObjectId(id))).first();
        } catch (IllegalArgumentException e) {
            doc = collection.find(new Document("_id", id)).first();
        }

        if (doc != null) {
            user user = new user();
            Object _id = doc.get("_id");
            if (_id instanceof ObjectId) {
                user.setId((ObjectId) _id);
            }
            user.setUserId(doc.getString("userId"));
            user.setName(doc.getString("name"));
            user.setEmail(doc.getString("email"));
            user.setMobileNumber(doc.getString("mobileNumber"));
            user.setAge(doc.getInteger("age"));
            user.setGender(doc.getString("gender"));
            return user;
        }

        return null;
    }

    // ✅ Delete user by userId
    public void deleteUserByUserId(String userId) {
        MongoCollection<Document> collection = getCollection();
        Document userDoc = collection.find(new Document("userId", userId)).first();
        
        if (userDoc != null) {
            // Delete the user by userId
            collection.deleteOne(new Document("userId", userId));
        }
    }

    // ✅ Update user by _id
public boolean updateUserByUserId(String userId, user user) {
    MongoCollection<Document> collection = getCollection();

    // Check if duplicate userId for different user (by _id)
    Document existing = collection.find(new Document("userId", user.getUserId())).first();

    if (existing != null && !existing.getString("userId").equals(userId)) {
        System.out.println("Duplicate userId found for a different user.");
        return false;
    }

    Document updatedDoc = new Document("userId", user.getUserId())
            .append("name", user.getName())
            .append("email", user.getEmail())
            .append("mobileNumber", user.getMobileNumber())
            .append("age", user.getAge())
            .append("gender", user.getGender());

    Document updateObject = new Document("$set", updatedDoc);
    collection.updateOne(new Document("userId", userId), updateObject);

    return true;
}


    // public boolean updateUserById(String id, user user) {
    //     MongoCollection<Document> collection = getCollection();

    //     // 1. Check if another user already has the same userId
    //     Document existing = collection.find(new Document("userId", user.getUserId())).first();

    //     if (existing != null && !existing.getObjectId("_id").toHexString().equals(id)) {
    //         // Another user has this userId
    //         System.out.println("Duplicate userId found for a different user.");
    //         return false;
    //     }

    //     // 2. Proceed with update
    //     Document updatedDoc = new Document("userId", user.getUserId())
    //             .append("name", user.getName())
    //             .append("email", user.getEmail())
    //             .append("mobileNumber", user.getMobileNumber())
    //             .append("age", user.getAge())
    //             .append("gender", user.getGender());

    //     Document updateObject = new Document("$set", updatedDoc);
    //     collection.updateOne(new Document("_id", new ObjectId(id)), updateObject);

    //     return true;
    // }

    // ✅ Check if userId already exists
    private boolean existsByUserId(String userId) {
        MongoCollection<Document> collection = getCollection();
        return collection.find(new Document("userId", userId)).first() != null;
    }

    // ✅ Generate the next userId
    private String generateNextUserId() {
        MongoCollection<Document> collection = getCollection();
        Document lastUser = collection.find()
                .sort(Sorts.descending("userId"))
                .first();

        if (lastUser == null || lastUser.getString("userId") == null) {
            return "U001";
        }

        String lastUserId = lastUser.getString("userId");
        int num = Integer.parseInt(lastUserId.substring(1)) + 1;
        return String.format("U%03d", num);
    }
}


// package com.example.Hello_world_api.repository;

// import com.example.Hello_world_api.model.user;
// import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoDatabase;
// import com.mongodb.client.model.Sorts;
// import org.bson.Document;
// import org.bson.types.ObjectId;
// import org.springframework.stereotype.Repository;

// import java.util.ArrayList;
// import java.util.List;

// @Repository
// public class userrepository {
//     private final MongoDatabase database;

//     public userrepository() {
//         this.database = MongoClients.create("mongodb://localhost:27017")
//                                      .getDatabase("Kx");
//     }

//     private MongoCollection<Document> getCollection() {
//         return database.getCollection("users");
//     }

//     // ✅ Create new user only (inserts document)
//     public String save(user user) {
//         MongoCollection<Document> collection = getCollection();

//         // Validate uniqueness
//         if (user.getUserId() != null && existsByUserId(user.getUserId())) {
//             return "UserId already exists!";
//         }

//         // Generate userId if not provided
//         if (user.getUserId() == null || user.getUserId().isEmpty()) {
//             user.setUserId(generateNextUserId());
//         }

//         Document doc = new Document("userId", user.getUserId())
//                 .append("name", user.getName())
//                 .append("email", user.getEmail())
//                 .append("mobileNumber", user.getMobileNumber())
//                 .append("age", user.getAge())
//                 .append("gender", user.getGender());

//         collection.insertOne(doc);
//         return "User created with ID: " + user.getUserId();
//     }

//     // ✅ Get all users
//     public List<user> getAllUsers() {
//         MongoCollection<Document> collection = getCollection();
//         List<user> users = new ArrayList<>();
//         for (Document doc : collection.find()) {
//             user user = new user();
//             user.setId(doc.getObjectId("_id"));
//             user.setUserId(doc.getString("userId"));
//             user.setName(doc.getString("name"));
//             user.setEmail(doc.getString("email"));
//             user.setMobileNumber(doc.getString("mobileNumber"));
//             user.setAge(doc.getInteger("age"));
//             user.setGender(doc.getString("gender"));
//             users.add(user);
//         }
//         return users;
//     }

//     // ✅ Get user by ID
//     public user getUserById(String id) {
//         MongoCollection<Document> collection = getCollection();
//         Document doc;

//         try {
//             doc = collection.find(new Document("_id", new ObjectId(id))).first();
//         } catch (IllegalArgumentException e) {
//             doc = collection.find(new Document("_id", id)).first();
//         }

//         if (doc != null) {
//             user user = new user();
//             Object _id = doc.get("_id");
//             if (_id instanceof ObjectId) {
//                 user.setId((ObjectId) _id);
//             }
//             user.setUserId(doc.getString("userId"));
//             user.setName(doc.getString("name"));
//             user.setEmail(doc.getString("email"));
//             user.setMobileNumber(doc.getString("mobileNumber"));
//             user.setAge(doc.getInteger("age"));
//             user.setGender(doc.getString("gender"));
//             return user;
//         }

//         return null;
//     }

//     // ✅ Delete user by ID
//     public void deleteUser(String id) {
//         MongoCollection<Document> collection = getCollection();
//         collection.deleteOne(new Document("_id", new ObjectId(id)));
//     }

//     // ✅ Update user by _id
//  public boolean updateUserById(String id, user user) {
//     MongoCollection<Document> collection = getCollection();

//     // 1. Check if another user already has the same userId
//     Document existing = collection.find(new Document("userId", user.getUserId())).first();

//     if (existing != null && !existing.getObjectId("_id").toHexString().equals(id)) {
//         // Another user has this userId
//         System.out.println("Duplicate userId found for a different user.");
//         return false;
//     }

//     // 2. Proceed with update
//     Document updatedDoc = new Document("userId", user.getUserId())
//             .append("name", user.getName())
//             .append("email", user.getEmail())
//             .append("mobileNumber", user.getMobileNumber())
//             .append("age", user.getAge())
//             .append("gender", user.getGender());

//     Document updateObject = new Document("$set", updatedDoc);
//     collection.updateOne(new Document("_id", new ObjectId(id)), updateObject);

//     return true;
// }


//     private boolean existsByUserId(String userId) {
//         MongoCollection<Document> collection = getCollection();
//         return collection.find(new Document("userId", userId)).first() != null;
//     }

//     private String generateNextUserId() {
//         MongoCollection<Document> collection = getCollection();
//         Document lastUser = collection.find()
//                 .sort(Sorts.descending("userId"))
//                 .first();

//         if (lastUser == null || lastUser.getString("userId") == null) {
//             return "U001";
//         }

//         String lastUserId = lastUser.getString("userId");
//         int num = Integer.parseInt(lastUserId.substring(1)) + 1;
//         return String.format("U%03d", num);
//     }

//     public void deleteUserByUserId(String userId) {
//     MongoCollection<Document> collection = getCollection();
//     Document userDoc = collection.find(new Document("userId", userId)).first();
    
//     if (userDoc != null) {
//         // Delete the user by userId
//         collection.deleteOne(new Document("userId", userId));
//     }
// }
// }


