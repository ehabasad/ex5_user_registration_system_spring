package hac.service;

import hac.model.Role;
import hac.model.User;
import hac.repository.RoleRepository;
import hac.repository.UserRepository;
import hac.controllers.UserRegistrationDto;
import hac.web.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private ApplicationConfig applicationConfig;

    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDto registrationDto, BindingResult result) {
        try {
            String username = registrationDto.getUsername();

            if (userRepository.existsByUsername(username)) {
                result.rejectValue("username", null, "There is already an account registered with that username");
                return null;
            }

            User existing = userRepository.findByEmail(registrationDto.getEmail());
            if (existing != null) {
                result.rejectValue("email", null, "There is already an account registered with that email");
                return null;
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(registrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setEnabled(true); // Assuming user is enabled by default
            user.setFullName("");
            user.setBio("");

            Role userRole = roleRepository.findByName("USER");
            user.setRoles(Collections.singleton(userRole));

            applicationConfig.addUser(username, registrationDto.getPassword());
            return userRepository.save(user);
        } catch (Exception e) {
            result.reject(null, "An error occurred during user registration. Please try again.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return true;
            } else {
                throw new IllegalArgumentException("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred during user login. Please try again later.");
        }
    }

    public User findUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while finding the user by username. Please try again later.");
        }
    }

    public User findUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while finding the user by email. Please try again later.");
        }
    }

    public User findByUsernameAndEmail(String username, String email) {
        try {
            return userRepository.findByUsernameAndEmail(username, email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while finding the user by username and email. Please try again later.");
        }
    }

    public void updateUserProfile(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while updating the user profile. Please try again later.");
        }
    }

    public void saveUserProfile(User updatedUser) {
        try {
            User existingUser = userRepository.findById(updatedUser.getId()).orElse(null);
            if (existingUser != null) {
                existingUser.setFullName(updatedUser.getFullName());
                existingUser.setBio(updatedUser.getBio());
                userRepository.save(existingUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while saving the user profile. Please try again later.");
        }
    }

    public void changePassword(String username, String newPassword) {
        try {
            applicationConfig.changePassword(username, newPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while changing the password. Please try again later.");
        }
    }


    public void resetUserPassword(String username, String password, String confirmPassword) {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("User with the given username does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while resetting the user password. Please try again later.");
        }
    }
}
