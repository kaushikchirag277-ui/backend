// package com.example.Hello_world_api;

// // public class HelloWorldController {
    
// // }

// // package com.example.hello_world_api;

// // import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.RestController;

// // @RestController
// // public class HelloWorldController {

// //     @GetMapping("/hell")
// //     public String sayHello() {
// //         return "Hello World";
// //     }
// // }

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.Hello_world_api.dto.NameRequest;
// import com.example.Hello_world_api.dto.GreetingResponse;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;

// @RestController
// public class HelloWorldController {

//     @GetMapping("/hello")
//     public Map<String, String> sayHello() {
//         Map<String, String> response = new HashMap<>();
//         response.put("message", "Hello World");
//         return response;
//     }
//     @PostMapping("/greet")
//     public GreetingResponse greetUser(@RequestBody NameRequest request) {
//         String name = request.getName();

//         // If the name is blank or null, set it to "User"
//         if (name == null || name.trim().isEmpty()) {
//             name = "User";
//         }

//         // Return the greeting message
//         return new GreetingResponse("Good morning " + name);
// }
// }

// // @RestController
// // public class GreetingController {

// //     @PostMapping("/greet")
// //     public GreetingResponse greetUser(@RequestBody NameRequest request) {
// //         String name = request.getName();

// //         // If the name is blank or null, set it to "User"
// //         if (name == null || name.trim().isEmpty()) {
// //             name = "User";
// //         }

// //         // Return the greeting message
// //         return new GreetingResponse("Good morning " + name);
// //     }
// // }

package com.example.Hello_world_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Hello_world_api.dto.NameRequest;
import com.example.Hello_world_api.dto.GreetingResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorldController {

    // This endpoint will return "Hello World"
    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World");
        return response;
    }

    // This endpoint will greet the user based on the name provided in the request body
    @PostMapping("/greet")
    public GreetingResponse greetUser(@RequestBody NameRequest request) {
        String name = request.getName();

        // If the name is blank or null, set it to "User"
        if (name == null || name.trim().isEmpty()) {
            name = "User";
        }

        // Return the greeting message
        return new GreetingResponse("Good morning " + name);
    }
    
}

