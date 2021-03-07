package com.aaca.land.controller;


import com.aaca.land.entity.User;
import com.aaca.land.common.UserConstant;
import com.aaca.land.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
//@Controller
@RestController
@RequestMapping("/user")
public class HomeController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public String joinGroup(@RequestBody User user){
        user.setRoles(UserConstant.DEFAULT_ROLE);
        String encryptedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);
        userRepository.save(user);
        return "HI " + user.getUserName() + " Welcome to group !";
    }
//    If loggedIn user is ADMIN -> ADMIN or MODERATOR
//    If loggedIn user is MODERATOR ->  MODERATOR

    @GetMapping("/access/{userId}/{userRole}")
//    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String giveAccessToUser(@PathVariable int userId,@PathVariable String userRole, Principal principal){
        User user = userRepository.findById(userId).get();
        List<String> activeRoles = getRolesByLoggedInUser(principal);
        String newRole = "";
        if (activeRoles.contains(userRole)){
            newRole = user.getRoles()+","+userRole;
            user.setRoles(newRole);
        }
        userRepository.save(user);
        return "Hi "+ user.getUserName() + " New Role assign to you by ! " + principal.getName();
    }
    @GetMapping
    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> loadUsers(){
        return userRepository.findAll();
    }
    @GetMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String testUserAccess(){
        return "user can only access this!";
    }

    private List<String> getRolesByLoggedInUser(Principal principal){
        String roles = getLoggedInUser(principal).getRoles();
        List<String> assignRoles = Arrays.stream(roles.split(",")).collect(Collectors.toList());
        if(assignRoles.contains("ROLE_ADMIN")){
            return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
        }
        if(assignRoles.contains("ROLE_MODERATOR")){
            return Arrays.stream(UserConstant.MODERATOR_ACCESS).collect(Collectors.toList());


        }
    return Collections.emptyList();
    }

    private User getLoggedInUser(Principal principal){
        return userRepository.findByUserName(principal.getName()).get();
    }


//   @RequestMapping("/")
//   public String homeMethod(){
//       return "home.html";
//   }
////    @GetMapping("/")
////    public String homeMethod(){
////        return ("<h1>Welcome</h1>");
////    }
//    @RequestMapping("/user")
//    public String userMethod() {
//        return "user.html";
//    }
//
////    @GetMapping("/user")
////        public String userMethod() {
////        return "<h1> Welcome User</h1>";
////    }
//
//    @RequestMapping("/admin")
//    public String adminMethod(){
//        return "admin.html";
//    }
////    @GetMapping("/admin")
////    public String adminMethod(){
////        return "<h1> Welcome Admin</h1>";
////    }
//


}
