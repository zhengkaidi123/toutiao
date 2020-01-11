package swjtu.zkd.toutiao.async;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EventModel {

    private EventType eventType;
    //触发者
    private int actorId;
    //触发对象
    private int entityId;
    private int entityType;
    //触发对象拥有者，拥有者会收到通知
    private int entityOwnerId;
    //保存触发现场信息
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {
    }

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    //链式操作
    public EventModel set(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String get(String key) {
        return exts.get(key);
    }
}
