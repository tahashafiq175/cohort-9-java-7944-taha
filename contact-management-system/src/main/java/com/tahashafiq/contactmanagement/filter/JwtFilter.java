package com.tahashafiq.contactmanagement.filter;
import com.tahashafiq.contactmanagement.service.JwtServiceImplementation;
import com.tahashafiq.contactmanagement.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils  jwtUtils;

    @Autowired
    JwtServiceImplementation jwtServiceImplementation;
    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException,IOException {
        String authorization = request.getHeader("Authorization");
        String userName=null;
        String jwt=null;
        if(authorization != null && authorization.startsWith("Bearer ")) {
           jwt = authorization.substring(7);
           userName=jwtUtils.extractUserName(jwt);
           if(userName!=null) {
               UserDetails userDetails = jwtServiceImplementation.loadUserByUsername(userName);
               if (jwtUtils.validateToken(jwt)){
                   UsernamePasswordAuthenticationToken auth =
                           new UsernamePasswordAuthenticationToken(userDetails,
                                   null,
                                   userDetails.getAuthorities());
                   auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder
                           .getContext()
                           .setAuthentication(auth);
               }
           }

        }
        chain.doFilter(request,response);
    }
}
