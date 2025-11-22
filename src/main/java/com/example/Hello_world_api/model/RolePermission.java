package com.example.Hello_world_api.model;

import java.util.List;

public class RolePermission {
    private String id; // Role name
    private List<String> permissions;

    public RolePermission() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}


