package com.example.gelda.user.service;

import com.example.gelda.security.JwtUtil;
import com.example.gelda.user.dto.*;
import com.example.gelda.user.entity.User;
import com.example.gelda.wallet.entity.Wallet;
import com.example.gelda.user.repository.UserRepository;
import com.example.gelda.wallet.service.WalletService;  // Import WalletService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WalletService walletService;  // Inject WalletService

    @Autowired
    public UserService(UserRepository userRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.walletService = walletService;  // Initialize WalletService
    }

    // Create new user with validation and wallet creation
    public String createUser(UserDTOCreate userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Email already exists.";
        }

        if (userRepository.findByNationalId(userDTO.getNationalId()).isPresent()) {
            return "National ID already exists.";
        }

        if (userRepository.findByMobileNumber(userDTO.getMobileNumber()).isPresent()) {
            return "Mobile number already exists.";
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User(
                userDTO.getName(),
                userDTO.getEmail(),
                hashedPassword,
                userDTO.getNationalId(),
                userDTO.getMobileNumber()
        );


        // Create and set wallet for the user
        Wallet wallet = new Wallet(user);  // Create new wallet for the user
        user.setWallet(wallet);  // Set wallet for the user

        userRepository.save(user);  // Save the user (wallet will be saved automatically because of cascade)
        return "User created successfully, and wallet created.";
    }

    public UserIdAndNameDTO getUserIdAndNameByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return new UserIdAndNameDTO(user.getId(), user.getName());
    }



    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Get user by mobile number
    public User getUserByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found with mobile number: " + mobileNumber));
    }

    // Update name and mobile number
    public User updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setMobileNumber(dto.getMobileNumber());

        return userRepository.save(user);
    }

    // Delete user if password matches
    public String deleteUser(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        userRepository.delete(user);
        return "User deleted successfully";
    }


    public String loginUser(LoginDTO loginDTO) {
        // Check if user exists by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginDTO.getEmail()));

        // Validate password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password.");
        }

        // Generate JWT token
        String token = JwtUtil.generateToken(user.getEmail());

        // Return the JWT token as part of the response
        return "Login successful. JWT Token: " + token;
    }

    public UserWalletInfoDTO getUserWalletInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserWalletInfoDTO(
                user.getName(),
                user.getWallet().getId(),
                user.getWallet().getBalance()
        );
    }

}
