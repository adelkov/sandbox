package com.hc.accounts.core.data;

import com.hc.accounts.core.data.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class UserDAOMemory implements UserDAOInterface {
    private Map<String, UserModel> users;

    public UserDAOMemory() {
        this.users = new HashMap<>();
        users.put("AK2018/1", new UserModel("Kovács Adél", "AK2018/1", 24234));
        users.put("AK2018/2", new UserModel("Kutya Cica", "AK2018/2", 23));
        users.put("AK2018/3", new UserModel("Béla Alma", "AK2018/3", -10));
    }

    public Map<String, UserModel> getUsers() {
        return this.users;
    }

    public UserModel addUser(UserModel user) {
        this.users.put(user.getUserId(), user);
        return user;
    }

    public UserModel getUserWithUserId(String id) {
        return this.users.get(id);
    }
}
