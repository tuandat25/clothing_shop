package com.tuandat.clothingshop.services.user;

import com.tuandat.clothingshop.component.JwtTokenUtil;
import com.tuandat.clothingshop.dtos.UserDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.exception.PermissionDenyException;
import com.tuandat.clothingshop.models.Role;
import com.tuandat.clothingshop.models.User;
import com.tuandat.clothingshop.repositories.RoleRepository;
import com.tuandat.clothingshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        // Register user
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("User is existed!");
        }

        if (!userDTO.getRetypePassword().equals(userDTO.getPassword())) {
            throw new RuntimeException("Retype password does not match!");
        }

        Role existingRole = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFaceBookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(existingRole);

        // Kiểm tra null trước khi gọi isEmpty()
        String googleAccountId = userDTO.getGoogleAccountId();
        String facebookAccountId = userDTO.getFaceBookAccountId();

        if ((googleAccountId == null || googleAccountId.isEmpty())
                && (facebookAccountId == null || facebookAccountId.isEmpty())) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        // Kiểm tra đăng nhập theo mật khẩu nếu không có social account
        String googleAccountId = user.getGoogleAccountId();
        String facebookAccountId = user.getFacebookAccountId();

        if ((googleAccountId == null || googleAccountId.isEmpty())
                && (facebookAccountId == null || facebookAccountId.isEmpty())) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
        }

        if (!user.isActive()) {
            throw new DataNotFoundException("User is locked!");
        }

        // Kiểm tra role của user (giả sử khách hàng có role "USER")
        if (!user.getRole().getName().equalsIgnoreCase("USER")) {
            throw new BadCredentialsException("User is not allowed to login via customer endpoint");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,
                password, user.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new Exception("Token expired!");
        }

        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("User not found");
        }
    }
}
