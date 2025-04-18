package chnu.edu.anetrebin.anb.controller;

import chnu.edu.anetrebin.anb.dto.requests.AccountRequest;
import chnu.edu.anetrebin.anb.dto.requests.CardRequest;
import chnu.edu.anetrebin.anb.dto.requests.UserRequest;
import chnu.edu.anetrebin.anb.dto.responses.UserResponse;
import chnu.edu.anetrebin.anb.service.user.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserServiceImpl service;

    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRequest userRequest) {
        service.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(service.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createAccount/{userId}")
    public ResponseEntity<Void> createAccount(@PathVariable Long userId, @RequestBody @Valid AccountRequest accountRequest) {
        service.createAccount(userId, accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addCard/{userId}")
    public ResponseEntity<Void> addCard(@PathVariable Long userId, @RequestBody @Valid CardRequest cardRequest) {
        service.addCard(userId, cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
