package swjtu.zkd.toutiao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swjtu.zkd.toutiao.dao.UserDAO;
import swjtu.zkd.toutiao.model.User;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

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
