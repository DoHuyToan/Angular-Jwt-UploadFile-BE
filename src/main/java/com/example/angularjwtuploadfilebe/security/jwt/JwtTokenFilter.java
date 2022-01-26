package com.example.angularjwtuploadfilebe.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Lớp này để gọi ra các hàm trong Lớp JwtProvider
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsService userDetailsService;

//    Tìm Token trong Request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //        Lấy Token trong Request
            String token = getJwt(request);
            if(token !=null && jwtProvider.validateToken(token)){
//            Lấy Username ra trong token này
                String username = jwtProvider.getUserNameFromToken(token);
//            Gán User của mình tìm đc trong DB với UserDetails của hệ thống
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
//      Set User này nên web của mình
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e){
            logger.error("Can't set user authentication -> Message: {}",e);
        }
        filterChain.doFilter(request, response);
    }

//    Get Token xong Request
    private String getJwt(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token !=null && token.startsWith("Bearer")){
            return token.replace("Bearer", "");
        }
        return null;
    }
}
