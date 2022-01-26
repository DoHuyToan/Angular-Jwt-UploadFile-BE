package com.example.angularjwtuploadfilebe.security.userprincal;

import com.example.angularjwtuploadfilebe.model.User;
import com.example.angularjwtuploadfilebe.repository.IUserRepository;
import com.example.angularjwtuploadfilebe.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserServiceImpl userService;

//    Hàm tìm User có tồn tại trên DB hay ko
//    orElseThrow(): bắt 1 cái ngoại lệ
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("User not found -> username or password" + username));
//        sau khi tìm ta phải build nó
        return UserPrinciple.build(user);
    }

    //  HÀM LẤY RA USER HIỆN TẠI ĐỂ THỰC HIỆN THAO TÁC VỚI DB
    public User getCurrentUser(){
        Optional<User> user;
        String userName;
        // Lấy 1 object principal trong SecurityContexHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // So sánh obj voi Userdetails nếu mà đúng thì gán userName = principal.getUsername();
        if(principal instanceof UserDetails){
            userName = ((UserDetails) principal).getUsername();
        } else {
            // nếu ko phải user hiện tại thì userName = principal.toString();
            userName = principal.toString();
        }
        // ktra nếu userName tồn tại trong DB thì gán user = hàm tìm kiếm trong DB theo userName
        if(userRepository.existsByUsername(userName)){
            user = userService.findByUsername(userName);
        } else {
            // Nếu chưa tồn tại thì trả về 1 thể hiện của lớp User thông qua Optional.of
            user = Optional.of(new User());
            // set cho no 1 cái tên user ẩn danh. Đây là trường hợp mà tương tác qua đăng nhập kiểu FB hay GG
            user.get().setUsername("Anonymous");
        }
        return user.get();
    }
}
