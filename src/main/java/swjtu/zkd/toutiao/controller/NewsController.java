package swjtu.zkd.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import swjtu.zkd.toutiao.model.*;
import swjtu.zkd.toutiao.service.*;
import swjtu.zkd.toutiao.util.ToutiaoUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @Autowired
    private AliService aliService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

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

    @GetMapping("/news/{newsId}")
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
                if (localUserId != 0) {
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, newsId));
                } else {
                    model.addAttribute("like", 0);
                }
                List<Comment> comments = commentService.getCommentByEntity(newsId, EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<>(comments.size());
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getUser(comment.getUserId()));
                    commentVOs.add(commentVO);
                }
                model.addAttribute("comments", commentVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        } catch (Exception e) {
            LOGGER.error("获取咨询明细错误", e);
        }
        return "detail";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("newsId") int newsId, @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            int count = commentService.getCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId, count);
        } catch (Exception e) {
            LOGGER.error("提交评论错误", e);
        }
        return "redirect:/" + newsId;
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
