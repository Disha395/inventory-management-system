package com.example.ims_backend.services;

import com.example.ims_backend.dto.LoginRequest;
import com.example.ims_backend.dto.RegisterRequest;
import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.UserDto;
import com.example.ims_backend.entity.User;

public interface IUserService {
    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response getUserById(Long id);

    Response updateUser(Long id, UserDto userDTO);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);
}
