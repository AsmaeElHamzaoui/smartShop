package com.smartShop.controller;


import com.smartShop.dto.LoginRequest;
import com.smartShop.dto.UserDto;
import com.smartShop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {

    private final UserService userService;

    // LOGIN
    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest request, HttpSession session) {

        UserDto userDto = userService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        // Stocker l'utilisateur dans la session
        session.setAttribute("user", userDto);

        return userDto;
    }

    // LOGOUT
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // détruit la session
        return "Logged out successfully";
    }



}
