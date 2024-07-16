package com.kream.root.admin.controller;


import com.kream.root.Login.model.SignInResultDTO;
import com.kream.root.Login.model.UserListDTO;
import com.kream.root.admin.domain.Address;
import com.kream.root.admin.domain.Admin;
import com.kream.root.admin.service.AdminService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/adminPage")
public class AdminController {
    @Autowired
    private AdminService adminService;

//    private final String uploadDir = "C:\\jsp_file\\";


    @GetMapping("/adminUser")
    public List<Admin> getAllAdmins() {
        System.out.println("adminUser = ");
        return adminService.findAdmins();
    }
    @GetMapping("/modifyAdmin/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable Long id) {
        Admin admin = adminService.findOne(id);
        return ResponseEntity.ok(admin);
    }
    @PostMapping("/loginCheck")
    public int loginCheck(@RequestBody Map<String, String> loginData) {
        String userId = loginData.get("userId");
        String userPw = loginData.get("userPw");

        Admin admin = adminService.findOne(Long.parseLong(userId));

       if(admin!=null&&admin.getPassword().equals(userPw)){
            return 1;
        }else {
           return 0;
       }
    }

    @PutMapping("/modifyAdmin/{id}")
    public ResponseEntity<Map<String, Object>> modifyAdmin(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        try {
            Admin existingAdmin = adminService.findOne(id);
            String filePath = existingAdmin.getProfilePicture();

            if (!file.isEmpty()) {
                filePath = saveFile(file, params.get("username"));
            }

            String address1 = params.get("address1");
            String[] addressParts = address1.split(", ");
            String zipcode = addressParts[0];
            String city = addressParts[1];
            String street = params.get("address2");

            Address address = new Address(city, street, zipcode);

            existingAdmin.setUsername(params.get("username"));
            existingAdmin.setPassword(params.get("password"));
            existingAdmin.setName(params.get("name"));
            existingAdmin.setAge(Integer.parseInt(params.get("age")));
            existingAdmin.setPhoneNumber(params.get("phoneNumber"));
            existingAdmin.setEmail(params.get("email"));
            existingAdmin.setAddress(address);
            existingAdmin.setAccessLevel(params.get("accessLevel"));
            existingAdmin.setRole(params.get("role"));
            existingAdmin.setStatus(params.get("status"));
            existingAdmin.setProfilePicture(filePath);
            existingAdmin.setDepartment(params.get("department"));
            existingAdmin.setNotes(params.get("notes"));

            adminService.updateAdmin(id, existingAdmin);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin updated successfully");
            response.put("profilePicture", filePath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to upload file"));
        }
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<Map<String, Object>> createAdmin(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params) {
        try {
            String filePath = "/default/path/to/file";

            if (!file.isEmpty()) {
                filePath = saveFile(file, params.get("username"));
            }

            String address1 = params.get("address1");
            String[] addressParts = address1.split(", ");
            String zipcode = addressParts[0];
            String city = addressParts[1];
            String street = params.get("address2");

            Address address = new Address(city, street, zipcode);

            Admin admin = new Admin();
            admin.setUsername(params.get("username"));
            admin.setPassword(params.get("password"));
            admin.setName(params.get("name"));
            admin.setAge(Integer.parseInt(params.get("age")));
            admin.setPhoneNumber(params.get("phoneNumber"));
            admin.setEmail(params.get("email"));
            admin.setAddress(address);
            admin.setAccessLevel(params.get("accessLevel"));
            admin.setRole(params.get("role"));
            admin.setStatus(params.get("status"));
            admin.setProfilePicture(filePath);
            admin.setDepartment(params.get("department"));
            admin.setNotes(params.get("notes"));

            adminService.join(admin);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin created successfully");
            response.put("profilePicture", filePath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to upload file"));
        }
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("C:/jsp_file/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String saveFile(MultipartFile file, String username) throws IOException {
        String folderPath = "C:/jsp_file/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = username + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(folderPath, fileName);
        Files.write(filePath, file.getBytes());

        return fileName;
    }



}
