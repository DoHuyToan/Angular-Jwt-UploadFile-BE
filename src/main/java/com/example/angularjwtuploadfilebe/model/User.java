package com.example.angularjwtuploadfilebe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
//        @UniqueConstraint: để username và email ko bị trùng nhau
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    ko đc để trống
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
//    id tự nhiên
    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
//    @JsonIgnore: khi truyền dữ liệu đi sẽ ko truyền password ra, ko lộ password của người dùng
    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
//    @Lob: để cho ko giới hạn độ dài
    @Lob
    private String avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            joinColumns = @JoinColumn(name = "role_id"))
//    dữ liệu ko trùng lặp nên dùng Set, ko dùng List
//    inverse: đối nghịch => 1 user chỉ có 1 quyền
    Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(@NotBlank @Size(min = 3, max = 50) String name,
                @NotBlank @Size(min = 3, max = 50) String username,
                @NotBlank @Size (max = 50) @Email String email,
                @NotBlank @Size(min = 6, max = 100) String encode,
                String avatar){
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = encode;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
