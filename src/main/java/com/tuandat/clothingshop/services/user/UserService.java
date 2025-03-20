package com.tuandat.clothingshop.services.user;

import com.tuandat.clothingshop.component.JwtTokenUtil;
import com.tuandat.clothingshop.dtos.UserDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.exception.FieldValidationException;
import com.tuandat.clothingshop.exception.PermissionDenyException;
import com.tuandat.clothingshop.models.Role;
import com.tuandat.clothingshop.models.User;
import com.tuandat.clothingshop.repositories.RoleRepository;
import com.tuandat.clothingshop.repositories.UserRepository;
import com.tuandat.clothingshop.responses.ProductResponse;
import com.tuandat.clothingshop.responses.UserResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
                .orElseThrow(() -> {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("phone_number", "Số điện thoại không tồn tại");
                    throw new FieldValidationException(errors);
                });

        // Kiểm tra đăng nhập theo mật khẩu nếu không có social account
        String googleAccountId = user.getGoogleAccountId();
        String facebookAccountId = user.getFacebookAccountId();

        if ((googleAccountId == null || googleAccountId.isEmpty())
                && (facebookAccountId == null || facebookAccountId.isEmpty())) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                Map<String, String> errors = new HashMap<>();
                errors.put("password", "Mật khẩu không đúng");
                throw new FieldValidationException(errors);
            }
        }

        if (!user.isActive()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("general", "User is locked!");
            throw new FieldValidationException(errors);
        }

        // Kiểm tra role của user (giả sử khách hàng có role "USER")
        // if (!user.getRole().getName().equalsIgnoreCase("USER")) {
        //     Map<String, String> errors = new HashMap<>();
        //     errors.put("general", "User is not allowed to login via customer endpoint");
        //     throw new FieldValidationException(errors);
        // }

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

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public UUID getRoleIdByPhoneNumber(String phoneNumber) throws DataNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return user.getRole().getId();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(user ->{
            var UserDTO = new UserDTO();
            UserDTO.setId(user.getId());
            UserDTO.setFullName(user.getFullName());
            UserDTO.setPhoneNumber(user.getPhoneNumber());
            UserDTO.setAddress(user.getAddress());
            UserDTO.setDateOfBirth(user.getDateOfBirth());
            UserDTO.setRoleName(user.getRole().getName());
            if(user.isActive()){
                UserDTO.setStatus("Active");
            }else{
                UserDTO.setStatus("Locked");
            }
            return UserDTO;
        }).toList();
    }

    @Override
    public UserDTO getById(UUID id) throws DataNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setRoleName(user.getRole().getName());
        if (user.isActive()) {
            userDTO.setStatus("Active");
        } else {
            userDTO.setStatus("Locked");
        }

        return userDTO;
    }
}
