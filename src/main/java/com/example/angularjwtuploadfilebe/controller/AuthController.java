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

    //    T???o User
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
    //        Check username v?? password ko ??c tr??ng
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
    //  X??? l?? Role
        Set<String> strRoles = signUpForm.getRoles();
    //  Set Role ????? Set n?? v??o
        Set<Role> roles = new HashSet<>();
    //  Check xem n?? l?? c??i g??, add v??o ????ng th???c tr???ng c???a n??, t??m ki???m n??, x??? l?? m???ng String tr??n
    //  Tr??n FE s??? g???i 1 ????ng k?? l?? ADMIN ho???c PM, USER => ph???i ph??n t??ch ra l?? th???ng n??o ????? l??u v??o DB
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
    //    Kh?? nh???t v?? d??? b??? l???i nh???t ??? h??m Login
    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody SigInForm sigInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(sigInForm.getUsername(), sigInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    //        T???o token, g???i h??m createToken ??? l???p JwtProvider => ph???i ti??m l???p JwtProvider
    //        authentication c???a h??? th???ng
        String token = jwtProvider.createToken(authentication);
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
    //        tr??? v??? l???p c?? tr?????ng name v?? jwtToken
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAvatar(), userPrinciple.getAuthorities()));
    }

    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(@RequestBody ChangeAvatar changeAvatar){
        // T??m User c?? t???n t???i hay ko?
        User user = userDetailService.getCurrentUser();
        if(user.getUsername().equals("Anonymous")){
            return new ResponseEntity<>(new ResponseMessage("Please login!"), HttpStatus.OK);
        }
        user.setAvatar(changeAvatar.getAvatar());
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
    }
}
