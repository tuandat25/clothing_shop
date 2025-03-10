package com.tuandat.clothingshop.services.user;

import com.tuandat.clothingshop.dtos.UserDTO;
import com.tuandat.clothingshop.exception.DataNotFoundException;
import com.tuandat.clothingshop.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    User getUserDetailsFromToken(String token) throws Exception;
}
