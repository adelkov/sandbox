package com.hc.accounts.core.data;

import java.math.BigDecimal;
import java.util.ArrayList;

public class UserDTOMemory implements UserDTOInterface {
    private ArrayList<UserModel> users;

    public UserDTOMemory() {
        this.users = new ArrayList<UserModel>() {{
            add(new UserModel("Kovács Adél", "AK2018/1", 24234));
            add(new UserModel("Almádi Balázs", "BA2018/3", 432434));
            add(new UserModel("Teszt Elek", "TE2013/4", -234));
        }};
    }

    public ArrayList<UserModel> getUsers() {
        return users;
    }

    public UserModel addUser(UserModel user) {
        this.users.add(user);
        return user;
    }

    public UserModel getUserWithUserId(String id) {
            for (UserModel user : this.users) {
                if (user.getUserId().equals(id)) return user;
            }
       return null;
    }

    public UserModel getUserWithUserName(String userName) {
        for (UserModel user : this.users) {
            if (user.getUserName().equals(userName)) return user;
        }
        return null;
    }


}
