package swjtu.zkd.toutiao.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoginTicket {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private int id;

    private int userId;

    private String ticket;

    private Date expired;

    //0有效，1无效
    private int status;
}
