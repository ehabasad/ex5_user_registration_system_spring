package hac.service;

import hac.model.User;
import hac.repository.UserRepository;
import hac.web.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private ApplicationConfig applicationConfig;


    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User enableUserAccount(String username) {
        try {
            applicationConfig.enableUser(username);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setEnabled(true);
                userRepository.save(user);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while enabling the user account. Please try again later.");
        }
    }

    @Override
    public User disableUserAccount(String username) {
        try {
            applicationConfig.disableUser(username);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setEnabled(false);
                userRepository.save(user);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while disabling the user account. Please try again later.");
        }
    }


    @Override
    public void deleteUserAccount(String username) {
        try {
            applicationConfig.deleteUser(username);
            User user = userRepository.findByUsername(username);
            if (user != null) {
                userRepository.delete(user);
            } else {
                throw new IllegalArgumentException("User with the given username does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while deleting the user account. Please try again later.");
        }
    }

}
