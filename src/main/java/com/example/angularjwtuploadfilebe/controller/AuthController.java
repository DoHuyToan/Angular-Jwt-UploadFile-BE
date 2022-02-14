package com.example.angularjwtuploadfilebe.controller;

import com.example.angularjwtuploadfilebe.dto.request.ChangeAvatar;
import com.example.angularjwtuploadfilebe.dto.request.SigInForm;
import com.example.angularjwtuploadfilebe.dto.request.SignUpForm;
import com.example.angularjwtuploadfilebe.dto.respone.JwtResponse;
import com.example.angularjwtuploadfilebe.dto.respone.ResponseMessage;
import com.example.angularjwtuploadfilebe.model.Role;
import com.example.angularjwtuploadfilebe.model.RoleName;
import com.example.angularjwtuploadfilebe.model.User;
import com.example.angularjwtuploadfilebe.security.jwt.JwtProvider;
import com.example.angularjwtuploadfilebe.security.jwt.JwtTokenFilter;
import com.example.angularjwtuploadfilebe.security.userprincal.UserDetailService;
import com.example.angularjwtuploadfilebe.security.userprincal.UserPrinciple;
import com.example.angularjwtuploadfilebe.service.impl.RoleServiceImpl;
import com.example.angularjwtuploadfilebe.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("")
@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @Autowired
    UserDetailService userDetailService;

    //    Tạo User
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
    //        Check username và password ko đc trùng
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("no_user"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("no_email"), HttpStatus.OK);
        }
        if (signUpForm.getAvatar() == null || signUpForm.getAvatar().trim().isEmpty()) {
            signUpForm.setAvatar("https://firebasestorage.googleapis.com/v0/b/chinhbeo-18d3b.appspot.com/o/avatar.png?alt=media&token=3511cf81-8df2-4483-82a8-17becfd03211");
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()), signUpForm.getAvatar());
    //  Xử lý Role
        Set<String> strRoles = signUpForm.getRoles();
    //  Set Role để Set nó vào
        Set<Role> roles = new HashSet<>();
    //  Check xem nó là cái gì, add vào đúng thực trạng của nó, tìm kiếm nó, xử lý mảng String trên
    //  Trên FE sẽ gửi 1 đăng ký là ADMIN hoặc PM, USER => phải phân tích ra là thằng nào để lưu vào DB
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(()-> new RuntimeException("Role not found"));
                    roles.add(adminRole);
                    break;
                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow( ()-> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
    }
    //    Khó nhất và dễ bị lỗi nhất ở hàm Login
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SigInForm sigInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(sigInForm.getUsername(), sigInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    //        Tạo token, gọi hàm createToken ở lớp JwtProvider => phải tiêm lớp JwtProvider
    //        authentication của hệ thống
        String token = jwtProvider.createToken(authentication);
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
    //        trả về lớp có trường name và jwtToken
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAvatar(), userPrinciple.getAuthorities()));
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(@RequestBody ChangeAvatar changeAvatar){
        // Tìm User có tồn tại hay ko?
        User user = userDetailService.getCurrentUser();
        if(user.getUsername().equals("Anonymous")){
            return new ResponseEntity<>(new ResponseMessage("Please login!"), HttpStatus.OK);
        }
        user.setAvatar(changeAvatar.getAvatar());
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
    }
}
