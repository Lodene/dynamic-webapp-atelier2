package com.sp.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class RequestUtils {
    private RequestUtils() {
    }

    public static Long getUserID(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies.length != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Cookie cookie = cookies[0];
        System.out.println(cookie.getName());
        return Long.parseLong(cookie.getValue());
    }
}