package com.mishanya.junit.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDaoSpy extends UserDao{

    private final UserDao userDao;

    public UserDaoSpy(UserDao userDao) {
        this.userDao = userDao;
    }

    private Map<Integer, Boolean> answers = new HashMap<>();

    @Override
    public boolean delete(Integer userId) {
        return answers.getOrDefault(userId, userDao.delete(userId));
    }
}
