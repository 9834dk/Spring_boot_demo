package com.example.service;

import com.example.Spring_boot_demo.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    UserDTO login(String username, String password);
    UserDTO updateUserInfo(long id, String email);
    void changePassword(long id, String oldPassword, String newPassword);
}
