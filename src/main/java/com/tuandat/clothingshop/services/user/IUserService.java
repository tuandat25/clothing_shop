package com.tuandat.clothingshop.services.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.tuandat.clothingshop.dtos.UserDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.models.User;


public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;

    UUID getRoleIdByPhoneNumber(String phoneNumber) throws DataNotFoundException;

    UserDTO getById(UUID id) throws DataNotFoundException;
    Long count();
    List<UserDTO> getAllUsers();
    // Page<UserDTO> getAllUsers(String keyword, Pageable pageable);
}
