package com.example.Hello_world_api.service;

// public class userservice {
    
// }


// package com.example.Hello_world_api.service;

import com.example.Hello_world_api.util.MongoUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class userservice {
    private final MongoCollection<Document> collection = MongoUtil.getUserCollection();

    public List<Document> getAllUsers() {
        return collection.find().into(new ArrayList<>());
    }

    public Document getUserById(String id) {
        return collection.find(Filters.eq("userId", id)).first();
    }

    public void createUser(Document user) {
        collection.insertOne(user);
    }

    public void updateUser(String id, Document user) {
        collection.replaceOne(Filters.eq("userId", id), user);
    }

    public void deleteUser(String id) {
        collection.deleteOne(Filters.eq("userId", id));
    }
}
