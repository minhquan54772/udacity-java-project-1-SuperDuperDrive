package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import static com.udacity.jwdnd.course1.cloudstorage.services.HashService.encodeSalt;

@Service
public class UserService {
    private final HashService hashService;
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.hashService = hashService;
        this.userMapper = userMapper;
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public int addUser(User user) {
        String salt = encodeSalt();
        String hashedPassword = hashService.getHashedValue(user.getPassword(), salt);

        return userMapper.insertUser(new User(user.getUsername(), hashedPassword, salt, user.getFirstname(), user.getLastname()));
    }
}
