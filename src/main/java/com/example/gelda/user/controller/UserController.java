package com.example.gelda.user.controller;

import com.example.gelda.user.dto.*;
import com.example.gelda.user.entity.User;
import com.example.gelda.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("USER")
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTOCreate userDTO) {
        String message = userService.createUser(userDTO);
        return ResponseEntity.ok(message);
    }

    @PreAuthorize("USER")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserIdAndNameDTO> getUserIdAndNameByEmail(@PathVariable String email) {
        UserIdAndNameDTO result = userService.getUserIdAndNameByEmail(email);
        return ResponseEntity.ok(result);
    }



    @PreAuthorize("USER")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("USER")
    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<User> getUserByMobileNumber(@PathVariable String mobileNumber) {
        User user = userService.getUserByMobileNumber(mobileNumber);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("USER")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        User updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("USER")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestBody DeleteUserDTO deleteUserDTO) {
        String result = userService.deleteUser(id, deleteUserDTO.getPassword());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("USER")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response); // Return the login success message or JWT token
    }

    @PreAuthorize("USER")
    @GetMapping("/{userId}/wallet-info")
    public ResponseEntity<UserWalletInfoDTO> getUserWalletInfo(@PathVariable Long userId) {
        UserWalletInfoDTO walletInfo = userService.getUserWalletInfo(userId);
        return ResponseEntity.ok(walletInfo);
    }

}