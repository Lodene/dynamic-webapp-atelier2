package com.sp.filter;

import com.sp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authorizationHeader = request.getHeader("Authorization");

        // Log des informations sur la requête entrante
        System.out.println("Incoming request: " + request.getRequestURI());
        System.out.println("Authorization header: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Si le token JWT est manquant ou invalide, renvoyer une erreur non autorisée
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authorizationHeader.substring(7);
        try {
            if (jwtUtil.validateToken(token)) {
                return true;
            } else {
                // Si le token JWT est invalide, renvoyer une erreur non autorisée
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        } catch (Exception e) {
            // En cas d'erreur lors de la validation du token JWT, renvoyer une erreur non autorisée
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
