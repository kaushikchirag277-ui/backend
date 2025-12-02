package com.example.Hello_world_api.repository;

import com.example.Hello_world_api.model.Store;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class StoreRepository {

    private MongoCollection<Document> collection;

    public StoreRepository() {
        MongoClient mongoClient = MongoClients.create("mongodb+srv://user:1234@cluster0.tltzqod.mongodb.net");
        MongoDatabase database = mongoClient.getDatabase("Kx");
        this.collection = database.getCollection("stores");
        System.out.println("Collection: " + collection.getNamespace());
    }

    public List<Store> findStoreNamesByIds(List<String> storeIds) {
        List<Store> storeList = new ArrayList<>();

        if (storeIds == null || storeIds.isEmpty()) return storeList;

        for (String id : storeIds) {
            Document doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                Store store = new Store();
                store.setId(doc.getString("_id"));
                store.setName(doc.getString("name"));
                store.setAddress(doc.getString("address"));
                store.setContactNumber(doc.getString("contactNumber"));
                storeList.add(store);
            }
        }

        return storeList;
    }
}
