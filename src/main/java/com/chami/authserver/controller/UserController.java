package com.chami.authserver.controller;

import com.chami.authserver.model.Role;
import com.chami.authserver.repository.RoleRepository;
import com.chami.authserver.repository.UserRepository;
import com.chami.authserver.service.UserService;
import com.chami.authserver.dao.UserDao;
import com.chami.authserver.model.ERole;
import com.chami.authserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    //get all the user details in the single api and also its admin role I added
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> listUser() {
        logger.info("started fetching the user details ");
        return userService.findAll();
    }

    //signup api for the users
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody UserDao user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("Username is already taken!");
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email is already in use!");
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        User user1 = new User(user.getUsername(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()));

        Set<String> strRoles = user.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "editor":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user1.setEnabled(true);
        user1.setRoles(roles);
        userRepository.save(user1);
        return ResponseEntity.ok("User registered successfully!");
    }


    // This api is public api any one can access to get the response
    @RequestMapping(value = "/getPublic",method = RequestMethod.GET)
    public String publicApi(){
        logger.info("public api started ");
        return "welcome to the real world";
    }

}
