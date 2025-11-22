package com.example.Hello_world_api.dto;

// public class GreetingResponse {
    
// // }
// package com.example.helloworldapi.dto;

public class GreetingResponse {
    private String message;

    // Constructor
    public GreetingResponse(String message) {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

