package com.kream.root.admin.controller;


import com.kream.root.Login.model.UserListDTO;
import com.kream.root.admin.domain.Admin;
import com.kream.root.admin.service.AdminMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/member")
public class adminMemberController {
    @Autowired
    AdminMemberService adminMemberService;

    @GetMapping("/memberuser")
    public List<UserListDTO> getAllUsers() {
        System.out.println("adminUser = ");
        return adminMemberService.getAllUsers();
    }
    @GetMapping("/modifyAdmin/{id}")
    public ResponseEntity<UserListDTO> getAdmin(@PathVariable Long id) {
        UserListDTO user = adminMemberService.findOne(id);
        return ResponseEntity.ok(user);
    }
}
