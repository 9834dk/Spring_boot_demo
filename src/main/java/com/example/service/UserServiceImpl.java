package com.example.service;

import com.example.Spring_boot_demo.dao.User;
import com.example.Spring_boot_demo.dao.UserRepository;
import com.example.Spring_boot_demo.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); 
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
            
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUserInfo(long id, String email) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
        if (StringUtils.hasLength(email)) {
            user.setEmail(email);
        }
        return convertToDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (!StringUtils.hasLength(newPassword)) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
