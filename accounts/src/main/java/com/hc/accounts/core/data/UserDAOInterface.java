package com.hc.accounts.core.data;

import com.hc.accounts.core.data.models.UserModel;
import java.util.Map;

interface UserDAOInterface {
    UserModel getUserWithUserId(String userId);

    UserModel addUser(UserModel user);

    Map<String, UserModel> getUsers();
}
