package com.example.simple_hris.controller;

import com.example.simple_hris.dto.request.UpdatePositionNameRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRequestDTO;
import com.example.simple_hris.dto.request.UpdateUserRoleRequestDTO;
import com.example.simple_hris.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserControlller {
    private final UserService userService;

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserRequestDTO request){
        userService.updateUser(id, request);
    }


    @PatchMapping("/role/{id}")
    public void updateUserRole(@PathVariable("id") Long id, @RequestBody UpdateUserRoleRequestDTO request){
        userService.updateUserRole(id, request);
    }

    @GetMapping("/cek-role")
    @PreAuthorize("hasAnyRole('SUPERADMIN','HRADMIN')")
    public String testRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current user: " + auth.getName());
        System.out.println("Authorities: " + auth.getAuthorities());
        return "ok";
    }
}
