package swjtu.zkd.toutiao.model;

import lombok.Data;

import java.util.Date;

@Data
public class News {

    private int id;

    private String title;

    private String link;

    private String image;

    private int likeCount;

    private int commentCount;

    private Date createdDate;

    private int userId;
}
