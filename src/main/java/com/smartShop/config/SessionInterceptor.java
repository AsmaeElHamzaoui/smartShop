package com.smartShop.config;

import com.smartShop.dto.UserDto;
import com.smartShop.enums.Role;
import com.smartShop.exception.ForbiddenException;
import com.smartShop.exception.UnauthorizedActionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
}
