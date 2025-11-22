package com.example.Hello_world_api.repository;

import com.example.Hello_world_api.model.RolePermission;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {

    private MongoCollection<Document> collection;

    public RoleRepository() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("Kx");
        this.collection = database.getCollection("roles");
    }

    public RolePermission getPermissionsByRole(String roleId) {
        Document doc = collection.find(new Document("_id", roleId)).first();

        if (doc != null) {
            RolePermission rp = new RolePermission();
            rp.setId(doc.getString("_id"));
            rp.setPermissions(doc.getList("permissions", String.class));
            return rp;
        }
        return null;
    }
}
