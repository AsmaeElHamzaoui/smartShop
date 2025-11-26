package com.smartShop.service;

import com.smartShop.dto.RegisterRequest;
import com.smartShop.dto.UserDto;
import com.smartShop.entity.User;
import com.smartShop.mapper.UserMapper;
import com.smartShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    // Méthode pour hasher le mot de passe
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 rounds
    }

    // CREATE
    public UserDto createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashPassword(request.getPassword())); // hash password
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // READ ALL
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ BY ID
    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    // UPDATE
    public UserDto updateUser(Integer id, RegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(hashPassword(request.getPassword())); // hash new password
        }
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    // DELETE
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
