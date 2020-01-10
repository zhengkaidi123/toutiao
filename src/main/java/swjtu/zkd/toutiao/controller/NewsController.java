package swjtu.zkd.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swjtu.zkd.toutiao.model.HostHolder;
import swjtu.zkd.toutiao.model.News;
import swjtu.zkd.toutiao.service.AliService;
import swjtu.zkd.toutiao.service.NewsService;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Date;

@Controller
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private AliService aliService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/user/addNews")
    @ResponseBody
    public String addNews(@RequestParam("image") String image, @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            LOGGER.error("添加咨询失败", e);
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadOSS(@RequestParam("file") MultipartFile file) {
        try {
            String fileURL = aliService.saveImage(file);
            if (fileURL == null) {
                return ToutiaoUtil.getJSONString(1, fileURL);
            }
            return ToutiaoUtil.getJSONString(0, fileURL);
        } catch (Exception e) {
            LOGGER.error("上传失败", e);
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    @GetMapping("/image")
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            Files.copy(new File(ToutiaoUtil.IMAGE_DIR + imageName).toPath(), new BufferedOutputStream(response.getOutputStream()));
        } catch (Exception e) {
            LOGGER.error("读取图片错误" + imageName + e.getMessage());
        }
    }
}
