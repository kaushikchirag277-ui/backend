package com.example.Hello_world_api.util;

// public class MongoUtil {
    
// }


// package com.example.Hello_world_api.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoUtil {
    private static final String CONNECTION_STRING = "mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net/";
    private static final String DB_NAME = "Kx";

    private static final MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);

    public static MongoCollection<Document> getUserCollection() {
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database.getCollection("users");
    }
     public static MongoCollection<Document> getUserLoginCollection() {
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        return database.getCollection("User_login"); // Make sure name matches MongoDB exactly
    }
}
