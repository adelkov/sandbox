package com.hc.accounts.core.data;

import java.util.ArrayList;

interface UserDTOInterface {
    public UserModel getUserWithUserId(String userId);
    public UserModel getUserWithUserName(String userName);
    public UserModel addUser(UserModel user);
    public ArrayList<UserModel> getUsers();
}
