package com.example.Booking.security;


import com.example.Booking.service.CustomUserDetailsService;
import com.example.Booking.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

//thực thi mỗi lần có reqquest gửi tới
public class JWtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUltils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        //Nếu authHeader là null hoặc rỗng, nghĩa là không có JWT trong yêu cầu,
        // filter sẽ tiếp tục chuỗi lọc và không làm gì thêm.
        if(authHeader ==null || authHeader.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtUltils.extractUsername(jwtToken);

        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= customUserDetailsService.loadUserByUsername(userEmail);
            if(jwtUltils.isValidToken(jwtToken, userDetails)){
                SecurityContext securityContext= SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);

            }
        }
        filterChain.doFilter(request,response);
    }
}
