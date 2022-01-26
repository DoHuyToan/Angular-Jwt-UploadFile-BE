package com.example.angularjwtuploadfilebe.security.userprincal;

import com.example.angularjwtuploadfilebe.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String avatar;
//    giống với kiểu dữ liệu của UserDetails chỗ hàm getAuthorities()
//    GrantedAuthority - cái này của hệ thống
    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple() {
    }

    public UserPrinciple(Long id, String name, String username, String email, String password, String avatar, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//  gán dữ liệu từ model(role) = Autho của UserDetails
    public static UserPrinciple build(User user){
//        chuyển từ Set sang List
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrinciple(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatar(),
                authorities
        );
    }

//    GrantedAuthority - cái này của hệ thống
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

//    ktra User đang hoạt động hay đã thoát ra rồi
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
