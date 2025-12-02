package com.example.Hello_world_api.repository;

// public class UserLoginRepository {
    
// }
//package com.example.Hello_world_api.repository;

import com.example.Hello_world_api.model.UserLogin;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public class UserLoginRepository {

    private MongoCollection<Document> collection;

    public UserLoginRepository() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net"); // Update if needed
        MongoDatabase database = mongoClient.getDatabase("Kx");     // Use your DB name
        this.collection = database.getCollection("User_login");
    }

public void updateSessionKey(String mobile, String sessionKey) {
    long expiryMillis = System.currentTimeMillis() + 30 * 60 * 1000; // 30 minutes from now
    collection.updateOne(
        new Document("mobile", mobile),
        Updates.combine(
            Updates.set("sessionKey", sessionKey),
            Updates.set("sessionExpiry", expiryMillis)
        )
    );
}

public boolean isValidSession(String sessionKey) {
    Document doc = collection.find(new Document("sessionKey", sessionKey)).first();
    if (doc == null) return false;

    Long expiry = doc.getLong("sessionExpiry");
    if (expiry == null) return false;

    return System.currentTimeMillis() < expiry;
}

    public UserLogin findByMobile(String mobile) {
    Document doc = collection.find(new Document("mobile", mobile)).first();

    if (doc != null) {
        UserLogin user = new UserLogin();

        // Handle _id as either ObjectId or String
        Object idField = doc.get("_id");
        if (idField instanceof ObjectId) {
            user.setId(((ObjectId) idField).toHexString());
        } else if (idField instanceof String) {
            user.setId((String) idField);
        } else {
            user.setId(null); // fallback
        }

        user.setName(doc.getString("name"));
        user.setMobile(doc.getString("mobile"));
        user.setRole(doc.getString("role"));

        // Convert shops array safely
        List<?> rawList = doc.getList("shops", Object.class);
        List<String> shops = new ArrayList<>();

        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof String) {
                    shops.add((String) obj);
                }
            }
        }

        user.setShops(shops);
        return user;
    }

    return null;
}

    // public UserLogin findByMobile(String mobile) {
    //     Document doc = collection.find(new Document("mobile", mobile)).first();

    //     if (doc != null) {
    //         UserLogin user = new UserLogin();
    //         user.setId(doc.getObjectId("_id").toHexString());
    //         user.setName(doc.getString("name"));
    //         user.setMobile(doc.getString("mobile"));
    //         user.setRole(doc.getString("role"));
    //         List<?> rawList = doc.getList("shops", Object.class);
    //         List<String> shops = new ArrayList<>();

    //         if (rawList != null) {
    //             for (Object obj : rawList) {
    //                 if (obj instanceof String) {
    //                     shops.add((String) obj);
    //                 }
    //             }
    //         }

    //         user.setShops(shops);

    //         // List<String> shops = (List<String>) doc.get("shops", List.class);
    //         // user.setShops(shops);

    //         return user;
    //     }

    //     return null;
    // }
}
