package swjtu.zkd.toutiao.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AliService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliService.class);

    public static final String URL = "https://lightnote.oss-cn-hangzhou.aliyun.com/";
    public static final String END_POINT = "oss-cn-shanghai.aliyuncs.com";
    public static final String ACCESS_KEY_ID = "ABCDEFG";
    public static final String ACCESS_KEY_SECRET = "nononono";
    public static final String BUCKET_NAME = "lightnote";

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().indexOf('.');
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        return upload(fileName, file);
    }

    private String upload(String fileName, MultipartFile file) {
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            PutObjectResult result = ossClient.putObject(BUCKET_NAME, fileName, new BufferedInputStream(file.getInputStream()));
            if (result == null) {
                return null;
            }
            System.out.println(result.getETag());
            return URL + fileName;
        } catch (IOException e) {
            LOGGER.error("上传失败", e);
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
