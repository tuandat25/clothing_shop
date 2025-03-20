package com.tuandat.clothingshop.filter;

import com.tuandat.clothingshop.component.JwtTokenUtil;
import com.tuandat.clothingshop.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${api.prefix}")
    private String apiPrefix;

    private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().replace("ROLE_", "")))
                .collect(Collectors.toList());
    }
    

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                System.out.println("Bypass JWT Filter for URL: " + request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            String token = null;
            // Kiểm tra token trong header "Authorization"
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                System.out.println("Token from Authorization header: " + token);
            }

            // Nếu không có token trong header, kiểm tra cookie
            if (token == null) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
                        if ("JWT_TOKEN".equals(cookie.getName())) {
                            token = cookie.getValue();
                            System.out.println("Token found in cookie: " + token);
                            break;
                        }
                    }
                } else {
                    System.out.println("No cookies found in request.");
                }
            }

            // Nếu không có token, không gửi lỗi mà cho phép request đi tiếp
            if (token == null) {
                System.out.println("No token found; proceeding without authentication.");
                filterChain.doFilter(request, response);
                return;
            }

            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            System.out.println("Extracted phoneNumber from token: " + phoneNumber);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                System.out.println("User Authorities (before mapping): " + userDetails.getAuthorities());
                Collection<? extends GrantedAuthority> mappedAuthorities = mapAuthorities(userDetails.getAuthorities());
                System.out.println("User Authorities (after mapping): " + mappedAuthorities);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, mappedAuthorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    System.out.println("Authentication set for user: " + userDetails.getUsername());
                    System.out.println("User Authorities: " + userDetails.getAuthorities());

                } else {
                    System.out.println("Token validation failed");
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("Exception in JWT filter: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isByPassToken(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.startsWith("/img/") ||
                path.startsWith("/lib/") ||
                path.startsWith("/mail/") ||
                path.startsWith("/vendor/") ||
                path.startsWith("/scss/")) {
            return true;
        }
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/orders", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/products/images/.*", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                // Pair.of(String.format("%s/users/details", apiPrefix), "GET")
                Pair.of("/auth/login", "GET"),
                Pair.of("/auth/register", "GET"));
        for (Pair<String, String> byPassToken : byPassTokens) {
            if (path.contains(byPassToken.getFirst())
                    && request.getMethod().equalsIgnoreCase(byPassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }

}
