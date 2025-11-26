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

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // 1) Vérifier si l'utilisateur est connecté
        if (session == null || session.getAttribute("user") == null) {
            throw new UnauthorizedActionException("Vous devez être connecté.");
        }

        UserDto user = (UserDto) session.getAttribute("user");
        Role role = user.getRole();

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Autoriser le logout pour tout utilisateur connecté
        if (path.startsWith("/auth/logout")) return true;


        // 2) ADMIN : accès total
        if (role == Role.ADMIN) {
            return true;
        }



        return false;
    }

}
