package swjtu.zkd.toutiao.model;

import lombok.Data;

@Data
public class User {

    private int id;

    private String name;

    private String password;

    private String salt;

    private String headUrl;

    public User() {}

    public User(String name) {
        this.name = name;
    }

}
