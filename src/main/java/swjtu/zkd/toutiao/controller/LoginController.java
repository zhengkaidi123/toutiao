package swjtu.zkd.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import swjtu.zkd.toutiao.model.LoginTicket;
import swjtu.zkd.toutiao.service.UserService;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        Map<String, Object> map = userService.register(username, password);
        try {
            if (map.isEmpty()) {
                map.put("msg", "注册成功");
                return ToutiaoUtil.getJSONString(LoginTicket.SUCCESS, map);
            } else {
                return ToutiaoUtil.getJSONString(LoginTicket.FAIL, map);
            }
        } catch (Exception e) {
            LOGGER.error("注册异常", e);
            return ToutiaoUtil.getJSONString(LoginTicket.FAIL, "注册异常" + e.getMessage());
        }
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        @RequestParam(value = "remember", defaultValue = "0") int remember, HttpServletResponse response) {
        Map<String, Object> map = userService.login(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remember > 0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(LoginTicket.SUCCESS, "登录成功");
            } else {
                return ToutiaoUtil.getJSONString(LoginTicket.FAIL, map);
            }
        } catch (Exception e) {
            LOGGER.error("登录异常", e);
            return ToutiaoUtil.getJSONString(LoginTicket.FAIL, "登录异常" + e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}
