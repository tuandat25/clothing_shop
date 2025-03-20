package com.tuandat.clothingshop.controllers;

import com.tuandat.clothingshop.component.LocalizationUtil;
import com.tuandat.clothingshop.dtos.UserDTO;
import com.tuandat.clothingshop.dtos.UserLoginDTO;
import com.tuandat.clothingshop.exception.FieldValidationException;
import com.tuandat.clothingshop.models.User;
import com.tuandat.clothingshop.responses.LoginResponse;
import com.tuandat.clothingshop.responses.RegisterResponse;
import com.tuandat.clothingshop.responses.UserResponse;
import com.tuandat.clothingshop.services.role.IRoleService;
import com.tuandat.clothingshop.services.user.IUserService;
import com.tuandat.clothingshop.utils.MessageKeys;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final LocalizationUtil localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        UUID roleId = UUID.fromString("9b42a237-c5f4-4fcb-a69f-e70555f4b0fe");
        if (userDTO.getRoleId() == null) {
            userDTO.setRoleId(roleId);
        }
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message(
                        localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH)).build());
            }

            User user = userService.createUser(userDTO);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
            BindingResult bindingResult,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            List<String> errorsMessage = bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorsMessage);
        }
        try {
            String token = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword());

            // Tạo cookie chứa token
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true); // bảo mật: không cho JS truy cập
            jwtCookie.setPath("/"); // gửi cookie kèm mọi request
            jwtCookie.setMaxAge(1 * 24 * 60 * 60); // thời gian sống, ví dụ 7 ngày
            response.addCookie(jwtCookie);
            var role_id = userService.getRoleIdByPhoneNumber(userLoginDTO.getPhoneNumber());
            var role_name = roleService.findRoleNameById(role_id);
            return ResponseEntity.ok(LoginResponse.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token)
                    .role(role_name)
                    .build());
        } catch (FieldValidationException ex) {
            // Trả về lỗi với HTTP 400 và map lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getFieldErrors());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, ex.getMessage()))
                            .build());
        }
    }

    @PostMapping("/details")
    // Needed 'Authorization' in RequestHeader
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) {
        try {
            String extractedToken = token.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        } catch (Exception ex) {
            return ResponseEntity.ok(LoginResponse.builder().message(
                    localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, ex.getMessage()))
                    .build());
        }
    }
}
