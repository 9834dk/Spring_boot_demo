package com.example.controller;

import com.Response;
import com.example.Spring_boot_demo.dto.UserDTO;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Response<UserDTO> register(@RequestBody UserDTO userDTO) {
        try {
            return Response.newSuccess(userService.register(userDTO));
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Response<UserDTO> login(@RequestBody UserDTO userDTO) {
        try {
            return Response.newSuccess(userService.login(userDTO.getUsername(), userDTO.getPassword()));
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Response<UserDTO> updateUserInfo(@PathVariable long id, @RequestParam(required = false) String email) {
        try {
            return Response.newSuccess(userService.updateUserInfo(id, email));
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    public Response<Void> changePassword(@PathVariable long id, 
                                         @RequestParam String oldPassword, 
                                         @RequestParam String newPassword) {
        try {
            userService.changePassword(id, oldPassword, newPassword);
            return Response.newSuccess(null);
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }
}
