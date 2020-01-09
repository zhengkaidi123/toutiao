package swjtu.zkd.toutiao.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.Ticket;
import swjtu.zkd.toutiao.dao.LoginTicketDAO;
import swjtu.zkd.toutiao.dao.UserDAO;
import swjtu.zkd.toutiao.model.LoginTicket;
import swjtu.zkd.toutiao.model.User;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    private static final int VALID = 0;
    private static final int INVALID = 1;

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已被注册");
            return map;
        }

        user = createUser(username, password);
        userDAO.addUser(user);
        user = userDAO.selectByName(username);
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }
        if (!user.getPassword().equals(ToutiaoUtil.MD5(password + user.getSalt()))) {
            map.put("msg", "密码错误");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, INVALID);
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(VALID);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));
        user.setHeadUrl(String.format("http://images:nowcoder.com/head/%dt.png", 1));
        return user;
    }

    public void addUser(User user) {
        userDAO.addUser(user);
    }

    public User getUser(int userId) {
        return userDAO.selectById(userId);
    }

    public void updateUserPassword(User user) {
        userDAO.updatePassword(user);
    }

    public void deleteUser(int userId) {
        userDAO.deleteById(userId);
    }


}
