package com.chami.authserver.service;



import com.chami.authserver.model.User;

import java.util.List;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
}
