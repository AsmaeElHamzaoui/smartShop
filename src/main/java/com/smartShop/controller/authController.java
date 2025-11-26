package com.smartShop.controller;


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





}
