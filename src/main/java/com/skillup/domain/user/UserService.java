package com.skillup.domain.user;

import com.skillup.infrastructure.jooq.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserDomain createUser(UserDomain userDomain) {
        // 和 infrastructure 交互
        // insert data into user table
        userRepository.createUser(userDomain);
        return userDomain;
    }

    public UserDomain readUserById(String accountId) {
        // return userDomain or null
        return userRepository.readUserById(accountId);
    }

    public UserDomain readUserByName(String accountName) {
        return userRepository.readUserByName(accountName);
    }

    public UserDomain updateUser(UserDomain userDomain) {
        userRepository.updateUser(userDomain);
        return userDomain;
    }

}
