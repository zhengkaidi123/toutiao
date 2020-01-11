package swjtu.zkd.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import swjtu.zkd.toutiao.util.JedisAdapter;
import swjtu.zkd.toutiao.util.RedisKeyUtil;

@Component
public class EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventModel.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            LOGGER.error("事件放入队列失败", e);
            return false;
        }
    }
}
