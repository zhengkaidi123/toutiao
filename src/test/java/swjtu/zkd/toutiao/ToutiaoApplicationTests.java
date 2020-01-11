package swjtu.zkd.toutiao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import swjtu.zkd.toutiao.dao.CommentDAO;
import swjtu.zkd.toutiao.dao.LoginTicketDAO;
import swjtu.zkd.toutiao.dao.NewsDAO;
import swjtu.zkd.toutiao.dao.UserDAO;
import swjtu.zkd.toutiao.model.EntityType;
import swjtu.zkd.toutiao.model.LoginTicket;
import swjtu.zkd.toutiao.model.News;
import swjtu.zkd.toutiao.model.User;

import java.util.Date;
import java.util.Random;

@Sql("classpath:init-schema.sql")
@SpringBootTest(classes = ToutiaoApplication.class)
class ToutiaoApplicationTests {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private CommentDAO commentDAO;

//    @Test
//    void contextLoads() {
//    }

    @Test
    void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword(String.valueOf(i));
            user.setSalt(String.valueOf(random.nextInt(1000)));

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://images.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);

            user.setPassword("newpassword");
            userDAO.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d", i+1));
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
        }
//        Assertions.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assertions.assertNull(userDAO.selectById(1));

        Assertions.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assertions.assertEquals(0, loginTicketDAO.selectByTicket("TICKET1").getStatus());
        Assertions.assertNotNull(commentDAO.selectByEntity(1, EntityType.ENTITY_NEWS));
    }

}
