package com.jason.fullstackbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public void addUser(@RequestBody User newUser) {
        userService.newUser(newUser);
    }

    @GetMapping("/users")
    public List<User> allUser() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setUsername(newUser.getUsername());
                    user.setEmail(newUser.getEmail());
                    return userService.saveUser(user);
                }).orElseThrow(()->new UserNotFoundException(id));
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userService.deleteById(id);
        return "User with id " + id + " has been deleted.";
    }
}
