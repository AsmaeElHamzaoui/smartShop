package com.smartShop.service;

import com.smartShop.dto.UserDto;
import com.smartShop.entity.User;
import com.smartShop.mapper.UserMapper;
import com.smartShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor   // Génère le constructeur pour les final fields
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // CREATE
    public UserDto createUser(UserDto dto, String rawPassword) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(rawPassword);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    // GET BY ID
    public UserDto getById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDTO(user);
    }

    // GET ALL
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    // UPDATE (username & role)
    public UserDto updateUser(Integer id, UserDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());

        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    // UPDATE PASSWORD
    public void updatePassword(Integer id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);  // normalement encoder !
        userRepository.save(user);
    }

    // DELETE
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User does not exist");
        }

        userRepository.deleteById(id);
    }

    // FIND BY USERNAME
    public UserDto getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDTO(user);
    }
}
