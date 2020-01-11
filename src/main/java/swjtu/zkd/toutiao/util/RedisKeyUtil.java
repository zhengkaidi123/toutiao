package swjtu.zkd.toutiao.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static final String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId, int entityType) {
        return BIZ_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getDislikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + entityId + SPLIT + entityType;
    }
}
