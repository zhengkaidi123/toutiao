package swjtu.zkd.toutiao.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;

@Component
public class JedisAdapter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool;

//    private Jedis jedis;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("localhost", 6379);
    }

    /*public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.flushAll();
        jedis.set("hello", "world");
        jedis.renamenx("hello", "newHello");
        jedis.setex("hello2", 10, "world");

        jedis.set("pv", "100");
        jedis.incr("pv");
        jedis.incrBy("pv", 4);

        String listName = "list";
        jedis.del(listName);
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + i);
        }
        System.out.println(jedis.lrange(listName, 0, 12));
        System.out.println(jedis.llen(listName));
        System.out.println(jedis.lpop(listName));
        System.out.println(jedis.lindex(listName, 3));
        System.out.println(jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "xx"));

        String userKey = "userxx";
        jedis.hset(userKey, "name", "jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "10086");
        System.out.println(jedis.hget(userKey, "name"));
        System.out.println(jedis.hgetAll(userKey));
        jedis.hdel(userKey, "phone");
        System.out.println(jedis.hexists(userKey, "email"));
        System.out.println(jedis.hkeys(userKey));
        System.out.println(jedis.hvals(userKey));

        String likeKey1 = "newsLike1";
        String likeKey2 = "newsLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i * 2));
        }
        System.out.println(jedis.smembers(likeKey1));
        System.out.println(jedis.sunion(likeKey1, likeKey2));
        System.out.println(jedis.sdiff(likeKey1, likeKey2));
        System.out.println(jedis.sinter(likeKey1, likeKey2));
        System.out.println(jedis.sismember(likeKey1, "12"));
        jedis.srem(likeKey1, "5");
        jedis.smove(likeKey1, likeKey2, "9");
        System.out.println(jedis.scard(likeKey2));

        String rankKey = "rankKey";
        jedis.zadd(rankKey, 15, "Jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 20, "Lucy");
        jedis.zadd(rankKey, 45, "Mei");

        System.out.println(jedis.zcard(rankKey));
        System.out.println(jedis.zcount(rankKey, 50, 99));
        System.out.println(jedis.zscore(rankKey, "Ben"));
        jedis.zincrby(rankKey, 3, "Lee");
        System.out.println(jedis.zscore(rankKey, "Lee"));
        System.out.println(jedis.zrange(rankKey, 0, 100));
        System.out.println(jedis.zrevrange(rankKey, 30, 70));
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rankKey, "40", "100")) {
            System.out.println(tuple.getElement() + ":" + tuple.getScore());
        }

        String setKey = "zset";
        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");
        System.out.println(jedis.zlexcount(setKey, "-", "+"));
        System.out.println(jedis.zlexcount(setKey, "(b", "[d"));
        System.out.println(jedis.zlexcount(setKey, "[b", "[d"));
        jedis.zrem(setKey, "b");
        System.out.println(jedis.zrange(setKey, 0, 10));

        jedis.close();
    }*/

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return null;
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
        }
    }

    public long sadd(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.sadd(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return 0;
        }
    }

    public long srem(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.srem(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return 0;
        }
    }

    public boolean sismember(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return false;
        }
    }

    public long scard(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.scard(key);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return 0;
        }
    }

    public void setex(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, 10, value);
        }  catch (Exception e) {
            LOGGER.error("发生异常", e);
        }
    }

    public long lpush(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return 0;
        }
    }

    public List<String> brpop(int timeout, String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            LOGGER.error("发生异常", e);
            return null;
        }
    }

    public void setObject(String key, String object) {
        set(key, JSON.toJSONString(object));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String value = get(key);
        if (value != null) {
            return JSON.parseObject(value, clazz);
        }
        return null;
    }
}
