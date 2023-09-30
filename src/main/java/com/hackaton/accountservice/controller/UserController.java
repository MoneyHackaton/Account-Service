package com.hackaton.accountservice.controller;

import com.hackaton.accountservice.dto.request.UserRequest;
import com.hackaton.accountservice.dto.response.DeleteResponse;
import com.hackaton.accountservice.dto.response.UserResponse;
import com.hackaton.accountservice.exception.NoEntityFoundException;
import com.hackaton.accountservice.model.ProfileModel;
import com.hackaton.accountservice.service.AccountService;
import com.hackaton.accountservice.service.UserService;
import com.hackaton.accountservice.service.dtoMapper.DtoMapper;
import com.hackaton.accountservice.service.dtoMapper.UserDtoMapper;
import com.hackaton.accountservice.service.serviceImpl.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "User Management", description = "Operations related to user accounts")
public class UserController {

    private final UserService userService;
    private final AccountServiceImpl accountService;

    private final DtoMapper<ProfileModel, UserRequest, UserResponse> userDtoMapper = new UserDtoMapper();

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        ProfileModel profileModel = userDtoMapper.toModel(userRequest);
        ProfileModel savedUser = userService.add(profileModel);
        UserResponse userResponse = userDtoMapper.toDto(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieve a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "User ID", required = true)
                                                    @PathVariable Long id) {
        ProfileModel profileModel = userService.getById(id);
        UserResponse userResponse = userDtoMapper.toDto(profileModel);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @ApiResponse(responseCode = "200", description = "Users found")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<ProfileModel> profileModels = userService.getAll();
        List<UserResponse> userResponses = profileModels.stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID", description = "Update a user with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        ProfileModel existingUser = userService.getById(id);
        ProfileModel updatedProfileModel = userDtoMapper.toModel(userRequest);
        updatedProfileModel.setId(existingUser.getId());
        ProfileModel savedUser = userService.update(updatedProfileModel);
        UserResponse userResponse = userDtoMapper.toDto(savedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID", description = "Delete a user by their ID")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUserById(@Parameter(description = "User ID", required = true)
                                               @PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/avatar")
    @Operation(summary = "Upload avatar for a user by ID", description = "Upload an avatar image for a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar uploaded"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<Object> uploadAvatar(@Parameter(description = "User ID", required = true) @PathVariable Long id,
                                               @RequestPart MultipartFile file) {
        try {
            accountService.uploadAvatar(id, file, "AccountAvatars");
            return ResponseEntity.ok().build();
        } catch (NoEntityFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DeleteResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse("Failed to upload avatar"));
        }
    }

    @DeleteMapping("/{id}/avatar")
    @Operation(summary = "Delete avatar for a user by ID", description = "Delete the avatar image for a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public ResponseEntity<Object> deleteAvatar(@Parameter(description = "User ID", required = true) @PathVariable Long id,
                                               @RequestParam String storageDirectory) {
        try {
            accountService.deleteAvatar(id, storageDirectory);
            return ResponseEntity.ok().build();
        } catch (NoEntityFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DeleteResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DeleteResponse("Failed to delete avatar"));
        }
    }
}
