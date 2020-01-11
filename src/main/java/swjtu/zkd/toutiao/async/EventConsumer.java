package swjtu.zkd.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import swjtu.zkd.toutiao.util.JedisAdapter;
import swjtu.zkd.toutiao.util.RedisKeyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventModel.class);

    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                for (EventType type : eventTypes) {
                    config.putIfAbsent(type, new ArrayList<>());
                    config.get(type).add(entry.getValue());
                }
            }
        }

        String key = RedisKeyUtil.getEventQueueKey();
        Thread thread = new Thread(() -> {
            while (true) {
                List<String> events = jedisAdapter.brpop(0, key);
                for (String msg : events) {
                    if (key.equals(msg)) {
                        continue;
                    }
                    EventModel eventModel = JSONObject.parseObject(msg, EventModel.class);
                    if (!config.containsKey(eventModel.getEventType())) {
                        LOGGER.error("不能识别的事件");
                        continue;
                    }
                    for (EventHandler eventHandler : config.get(eventModel.getEventType())) {
                        eventHandler.doHandle(eventModel);
                    }
                }
            }
        });
        thread.start();
    }
}
