package com.nexus.server.service;

import com.nexus.server.entities.User;
import com.nexus.server.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by id
     * @param id User id
     * @return User
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by username
     * @param username Username
     * @return User
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Create user
     * @param user User
     * @return User
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update user
     * @param id User id
     * @param userDetails User details
     * @return User
     */
    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(userDetails.getRole());
                    user.setUsername(userDetails.getUsername());
                    user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
                    user.setDui(userDetails.getDui());
                    user.setEmail(userDetails.getEmail());
                    user.setGender(userDetails.getGender());
                    user.setBirthday(userDetails.getBirthday());
                    return userRepository.save(user);
                });
    }

    /**
     * Delete user
     * @param id User id
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}