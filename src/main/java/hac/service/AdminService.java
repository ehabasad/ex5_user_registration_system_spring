package hac.service;

import hac.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User enableUserAccount(String username);

    User disableUserAccount(String username);

//    void deleteUserAccount(Long id );
    void deleteUserAccount(String username );

}
