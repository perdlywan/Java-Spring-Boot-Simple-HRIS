package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.UpdateUserRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRoleRequestDTO;
import com.example.simple_hris.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequestDTO request){
        userService.updateUser(id, request);
    }


    @PatchMapping("/role/{id}")
    public void updateUserRole(@PathVariable("id") Long id, @RequestBody UpdateUserRoleRequestDTO request){
        userService.updateUserRole(id, request);
    }
}
