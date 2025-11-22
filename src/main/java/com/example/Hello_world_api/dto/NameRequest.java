package com.example.Hello_world_api.dto;

// public class NameRequest {
    
// }

// package com.example.helloworldapi.dto;

public class NameRequest {
    private String name;

    public NameRequest() {
    }

    public NameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

