package swjtu.zkd.toutiao.async.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swjtu.zkd.toutiao.async.EventHandler;
import swjtu.zkd.toutiao.async.EventModel;
import swjtu.zkd.toutiao.async.EventType;
import swjtu.zkd.toutiao.model.Message;
import swjtu.zkd.toutiao.service.MessageService;
import swjtu.zkd.toutiao.util.MailSender;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private MessageService messageService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getEntityOwnerId());
        message.setToId(eventModel.getActorId());
        message.setContent("登录异常");
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.getActorId());
        mailSender.sendWithHtmlTemplate(eventModel.get("email"), "登录异常", "mails/welcome.html", map);

    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
