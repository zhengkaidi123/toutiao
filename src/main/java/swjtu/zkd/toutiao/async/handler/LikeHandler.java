package swjtu.zkd.toutiao.async.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swjtu.zkd.toutiao.async.EventHandler;
import swjtu.zkd.toutiao.async.EventModel;
import swjtu.zkd.toutiao.async.EventType;
import swjtu.zkd.toutiao.model.Message;
import swjtu.zkd.toutiao.model.User;
import swjtu.zkd.toutiao.service.MessageService;
import swjtu.zkd.toutiao.service.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LikeHandler.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        User user = userService.getUser(eventModel.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的资讯" + ", http://127.0.0.1:8888/news/"
                + eventModel.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        LOGGER.info(eventModel.toString());
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
