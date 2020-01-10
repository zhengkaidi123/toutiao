package swjtu.zkd.toutiao.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class ToutiaoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToutiaoUtil.class);
    public static final String[] IMAGE_FILE_EXT = {"png", "jpg", "jpeg", "bmp"};
    public static final String IMAGE_DIR = "D:\\toutiao\\upload";
    public static final String TOUTIAO_DOMAIN = "http://127.0.0.1:8888";

    public static String MD5(String key) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = key.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md5 = mdInst.digest();
            int j = md5.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md5[i];
                str[k++] = hexDigits[byte0 & 0xf];
                str[k++] = hexDigits[(byte0 >>> 4) & 0xf];
            }
            return String.valueOf(str);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("生成MD5失败", e);
            return null;
        }
    }

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.putAll(map);
        return json.toJSONString();
    }

    public static boolean isFileAllowed(String fileExt) {
        for (String ext : IMAGE_FILE_EXT) {
            if (ext.equals(fileExt)) {
                return true;
            }
        }
        return false;
    }
}
