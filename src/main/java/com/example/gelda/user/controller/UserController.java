package com.example.gelda.user.controller;



import com.example.gelda.user.dto.*;
import com.example.gelda.user.entity.User;
import com.example.gelda.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTOCreate userDTO) {
        String message = userService.createUser(userDTO);
        return ResponseEntity.ok(message);
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable String email) {
        Long userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }


    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Get user by mobile number
    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<User> getUserByMobileNumber(@PathVariable String mobileNumber) {
        User user = userService.getUserByMobileNumber(mobileNumber);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        User updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestBody DeleteUserDTO deleteUserDTO) {
        String result = userService.deleteUser(id, deleteUserDTO.getPassword());
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{userId}/add-friend")
    public ResponseEntity<String> addFriend(
            @PathVariable Long userId,
            @RequestBody AddFriendDTO dto
    ) {
        return ResponseEntity.ok(userService.addFriend(userId, dto.getFriendMobileNumber()));
    }

    @GetMapping("/{userId}/friends")
    public List<FriendDTO> getUserFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @DeleteMapping("/{userId}/remove-friend")
    public ResponseEntity<String> removeFriend(
            @PathVariable Long userId,
            @RequestBody RemoveFriendDTO dto
    ) {
        return ResponseEntity.ok(userService.removeFriendByMobileNumber(userId, dto.getFriendMobileNumber()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response); // Return the login success message or JWT token
    }

    @GetMapping("/{userId}/wallet-info")
    public ResponseEntity<UserWalletInfoDTO> getUserWalletInfo(@PathVariable Long userId) {
        UserWalletInfoDTO walletInfo = userService.getUserWalletInfo(userId);
        return ResponseEntity.ok(walletInfo);
    }





}

