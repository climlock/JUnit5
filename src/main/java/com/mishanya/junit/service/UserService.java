package com.mishanya.junit.service;

import com.mishanya.junit.dto.User;

import java.util.*;

public class UserService {

    private final List<User> users = new ArrayList<>();

    public List<User> getAll() {
        return users;
    }

    public boolean add(User user) {
        return users.add(user);
    }

    public Optional<User> login(String username, String password) {
        return users.stream()
                .filter(user -> user.getPassword().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }
}
