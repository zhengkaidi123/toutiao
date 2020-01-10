package swjtu.zkd.toutiao.controller;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swjtu.zkd.toutiao.model.HostHolder;
import swjtu.zkd.toutiao.model.Message;
import swjtu.zkd.toutiao.model.User;
import swjtu.zkd.toutiao.model.ViewObject;
import swjtu.zkd.toutiao.service.MessageService;
import swjtu.zkd.toutiao.service.UserService;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
    
    @Autowired
    private HostHolder hostHolder;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageService messageService;

    @GetMapping("/msg/detail")
    public String conversationDetail(@RequestParam("conversationId") String conversationId, Model model) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>(messageList.size());
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("username", user.getName());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            LOGGER.error("获取会话详细内容失败", e);
        }
        return "letterDetail";
    }

    @GetMapping("/msg/list")
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<Message> messageList = messageService.getConversationList(localUserId, 0, 10);
            List<ViewObject> conversations = new ArrayList<>(messageList.size());
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("username", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unReadCount", messageService.getUnReadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            LOGGER.error("获取站内信列表失败", e);
        }
        return "letter";
    }

    @RequestMapping(value = "/msg/addMessage", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId, @RequestParam("content") String content) {
        Message msg = new Message();
        msg.setContent(content);
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setCreatedDate(new Date());
        msg.setConversationId(String.format("%d_%d", Math.min(fromId, toId), Math.max(fromId, toId)));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
}
