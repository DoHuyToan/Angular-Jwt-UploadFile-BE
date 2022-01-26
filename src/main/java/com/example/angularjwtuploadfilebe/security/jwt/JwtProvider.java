package com.example.angularjwtuploadfilebe.security.jwt;

import com.example.angularjwtuploadfilebe.security.userprincal.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

//  Lớp này để tạo ra Token
@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
//    Set thời gian sống của Token
    private String jwtSecret = "toan.do@codegym.vn";
//    Time sống của Token, 86400 là số giây của 1 ngày
    private int jwtExpiration = 86400;
//    Sau khi login, hàm sau tạo Token
//    Jwts: mã hóa đc thành Token
    public String createToken(Authentication authentication){
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpiration*1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

//    Ktra xem Token có hợp lệ hay ko
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);  // parseClaimsJws: bắn ra các ngoại lệ
            return true;
        } catch (SignatureException e){
            logger.error("Invalid JWT signature -> Message: {}", e);
        }catch (MalformedJwtException e){
            logger.error("Invalid format Token -> Message: {}",e);
        } catch (ExpiredJwtException e){
            logger.error("Expired JWT token -> Message: {}",e);
        } catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT token -> Message: {}",e);
        } catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty --> Message {}",e);
        }
        return false;
    };

//    Khi gửi Request, có Token rồi, ta tìm Username trong Token đấy là gì
    public String getUserNameFromToken(String token){
        String userName = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }
}
