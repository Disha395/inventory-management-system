package com.example.ims_backend.services.impl;

import com.example.ims_backend.dto.LoginRequest;
import com.example.ims_backend.dto.RegisterRequest;
import com.example.ims_backend.dto.Response;
import com.example.ims_backend.dto.UserDto;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.enums.UserRole;
import com.example.ims_backend.exceptions.InvalidCredentialsException;
import com.example.ims_backend.exceptions.NotFoundException;
import com.example.ims_backend.repository.UserRepository;
import com.example.ims_backend.security.JwtUtils;
import com.example.ims_backend.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    private static final String DEFAULT_EXPIRATION = "6 months";

    // ================= REGISTER =================
    @Override
    public Response registerUser(RegisterRequest registerRequest) {

        // Prevent duplicate emails
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        UserRole role = UserRole.MANAGER;

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User registered successfully!")
                .build();
    }

    // ================= LOGIN =================
    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime(DEFAULT_EXPIRATION)
                .build();
    }

    // ================= GET ALL USERS =================
    @Override
    public Response getAllUsers() {

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        users.forEach(user -> user.setTransactions(null));

        Type listType = new TypeToken<List<UserDto>>() {}.getType();
        List<UserDto> userDTOS = modelMapper.map(users, listType);

        return Response.builder()
                .status(200)
                .message("success")
                .users(userDTOS)
                .build();
    }

    // ================= GET CURRENT LOGGED USER =================
    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setTransactions(null);
        return user;
    }

    // ================= GET USER BY ID =================
    @Override
    public Response getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserDto userDTO = modelMapper.map(user, UserDto.class);
        userDTO.setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }

    // ================= UPDATE USER =================
    @Override
    public Response updateUser(Long id, UserDto userDTO) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userDTO.getEmail() != null)
            existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPhoneNumber() != null)
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        if (userDTO.getName() != null)
            existingUser.setName(userDTO.getName());

        if (userDTO.getRole() != null)
            existingUser.setRole(userDTO.getRole());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            existingUser.setPassword(
                    passwordEncoder.encode(userDTO.getPassword())
            );
        }

        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    // ================= DELETE USER =================
    @Override
    public Response deleteUser(Long id) {

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("User successfully deleted")
                .build();
    }

    // ================= GET USER TRANSACTIONS =================
    @Override
    public Response getUserTransactions(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserDto userDTO = modelMapper.map(user, UserDto.class);

        userDTO.getTransactions().forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .user(userDTO)
                .build();
    }
}