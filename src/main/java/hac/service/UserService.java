package hac.service;

import hac.model.User;
import hac.controllers.UserRegistrationDto;
import org.springframework.validation.BindingResult;

public interface UserService {


    User registerUser(UserRegistrationDto registrationDto, BindingResult result);

    boolean loginUser(String username, String password);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    void updateUserProfile(User user);

    void saveUserProfile(User updatedUser);

    void changePassword(String username, String newPassword);

    User findByUsernameAndEmail(String username, String email);

    void resetUserPassword(String username,String password, String confirmPassword) ;
}