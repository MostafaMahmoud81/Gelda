package com.example.gelda.user.service;

import com.example.gelda.user.dto.UserDTOCreate;
import com.example.gelda.user.dto.UpdateUserDTO;
import com.example.gelda.user.dto.FriendDTO;
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

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
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

    // Add a friend
    public String addFriend(Long userId, String friendMobileNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findByMobileNumber(friendMobileNumber)
                .orElseThrow(() -> new RuntimeException("Friend not found with mobile number"));

        if (user.getId().equals(friend.getId())) {
            throw new RuntimeException("You cannot add yourself as a friend.");
        }

        // Avoid adding duplicates
        if (user.getFriends().contains(friend)) {
            return "Friend already added.";
        }

        // Add each user to the other's friends list
        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userRepository.save(user);
        userRepository.save(friend);

        return "Friend added successfully.";
    }

    // Get friends of a user
    public List<FriendDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends().stream()
                .map(friend -> new FriendDTO(friend.getName(), friend.getEmail(), friend.getMobileNumber()))
                .collect(Collectors.toList());
    }

    // Remove a friend
    public String removeFriendByMobileNumber(Long userId, String friendMobileNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findByMobileNumber(friendMobileNumber)
                .orElseThrow(() -> new RuntimeException("Friend not found with this mobile number"));

        if (!user.getFriends().contains(friend)) {
            throw new IllegalArgumentException("This user is not your friend.");
        }

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userRepository.save(user);
        userRepository.save(friend);

        return "Friend removed successfully.";
    }
}
