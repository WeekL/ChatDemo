package weekl.chatdemo.model;

import weekl.chatdemo.R;

public class User {
    public String name;
    public int avatarId;

    public User() {
        avatarId = R.mipmap.app_icon;
    }

    public User(String name){
        this();
        this.name = name;
    }
}
