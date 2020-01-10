package swjtu.zkd.toutiao.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import swjtu.zkd.toutiao.model.Comment;

import java.util.List;

@Mapper
public interface CommentDAO {

    String TABLE_NAME = "comment";
    String INSERT_FIELDS = " user_id, entity_id, entity_type, content, created_date, status ";
    String SELECT_FIELDS = " id, user_id, entity_id, entity_type, content, created_date, status ";

    @Insert({"insert into ", TABLE_NAME, " (", INSERT_FIELDS, ") values (#{userId}, #{entityId}, " +
            "#{entityType}, #{content}, #{createdDate}, #{status})"})
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS,  "from ", TABLE_NAME, " where entity_id=#{entityId} and entity_Type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_Type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
