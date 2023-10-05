package com.skillup.domain.user;

public interface UserRepository {
    void createUser(UserDomain userDomain);

    UserDomain readUserById(String id);

    UserDomain readUserByName(String name);

    void updateUser(UserDomain userDomain);
}
