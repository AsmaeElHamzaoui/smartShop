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


        // 3) CLIENT
        if (role == Role.CLIENT) {

            // CLIENT n'a QUE des GET
            if (!method.equals("GET")) {
                throw new ForbiddenException("Les clients ne peuvent pas modifier les données.");
            }

            // Interdiction totale d'accéder à des endpoints admin
            if (path.startsWith("/api/users") || path.startsWith("/admin")) {
                throw new ForbiddenException("Accès réservé aux administrateurs.");
            }

            // Produits accessible
            if (path.startsWith("/api/products")) {
                return true;
            }

            // Profil (uniquement son propre)
            if (path.startsWith("/profile")) {
                return true;
            }

            // Commandes personnelles
            if (path.startsWith("/commandesPersonnal")) {
                return true;
            }

            // Statistiques perso
            if (path.startsWith("/statisticsPersonnal")) {
                return true;
            }

            // Toute autre endpoint = interdit pour client
            throw new ForbiddenException("Accès non autorisé pour un client.");
        }



        return false;
    }

}
