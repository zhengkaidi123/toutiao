package swjtu.zkd.toutiao.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import swjtu.zkd.toutiao.model.News;

import java.util.List;

@Mapper
public interface NewsDAO {

    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, title, link, image, like_count, comment_count, created_date, user_id  ";

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{title}, #{link}, #{image}, #{likeCount}, " +
            "#{commentCount}, #{createdDate}, #{userId})"})
    int addNews(News news);

    // 使用xml来配置
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);
}
