package com.example.Hello_world_api.controller;

import com.example.Hello_world_api.repository.RoleRepository;
import com.example.Hello_world_api.model.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Hello_world_api.dto.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class RolePermissionController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<?> getPermissionsByRole(@RequestBody RoleRequest request) {
        RolePermission rolePermission = roleRepository.getPermissionsByRole(request.getRole());

        if (rolePermission != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("role", rolePermission.getId());
            response.put("permissions", rolePermission.getPermissions());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Role not found");
        }
    }
}
