package swjtu.zkd.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import swjtu.zkd.toutiao.model.User;
import swjtu.zkd.toutiao.service.ToutiaoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(value = {"/", "/index"})
    @ResponseBody
    public String index(HttpSession session) {
        toutiaoService.say();
        LOGGER.info("index");
        LOGGER.warn("warn in the index");
        return "Hello World <br> " + session.getAttribute("name") + "<br>" + toutiaoService.say();
    }

    @RequestMapping(value = "profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId, @PathVariable("userId") int userId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key) {
        return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}", groupId, userId, type, key);
    }

    @RequestMapping("/vm")
    public String news(Model model) {
        model.addAttribute("key", "zkd");
        List<String> colors = Arrays.asList("red", "blue", "green");
        model.addAttribute("colors", colors);
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(i, String.valueOf(i));
        }
        model.addAttribute("map", map);
        User user = new User();
        user.setName("jim");
        model.addAttribute("user", user);
        return "news";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        StringBuffer sb = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name).append(":").append(request.getHeader(name)).append("<br>");
        }
        for (Cookie cookie : request.getCookies()) {
            sb.append("cookie: ").append(cookie.getName()).append(":").append(cookie.getValue()).append("<br>");
        }
        sb.append("getMethod").append(request.getMethod()).append("br");
        sb.append("getPathInfo").append(request.getPathInfo()).append("br");
        sb.append("getSession").append(request.getSession()).append("br");
        sb.append("getRequestURL").append(request.getRequestURL()).append("br");
        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(HttpServletRequest request, HttpServletResponse response,
                           @CookieValue(value = "nowcoderId", defaultValue = "a") String nowcoderId,
                           @RequestParam("key") String key, @RequestParam("value") String value) {
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "NowcoderId from cookie: " + nowcoderId;
    }

    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code) {
        RedirectView redirectView = new RedirectView("/", true);
        if (code == 301) {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
//        if (code == 302) {
//            redirectView.setStatusCode(HttpStatus.MOVED_TEMPORARILY);
//        }
        return redirectView;
    }

    @RequestMapping("/red")
    public String red(HttpSession session) {
        session.setAttribute("name", "zkd");
        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam("name") String name) {
        if ("admin".equals(name)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("无管理员权限");
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String error(Exception e) {
        return "error: " + e.getMessage();
    }
}
