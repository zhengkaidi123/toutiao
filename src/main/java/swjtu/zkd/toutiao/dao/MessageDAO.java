package swjtu.zkd.toutiao.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import swjtu.zkd.toutiao.model.Message;

import java.util.List;

@Mapper
public interface MessageDAO {

    String TABLE_NAME = "message";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, from_id, to_id, content, created_date, has_read, conversation_id ";

    @Insert({"insert into ", TABLE_NAME, " (", INSERT_FIELDS, ") values (#{fromId}, #{toId}, #{content}, " +
            "#{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", INSERT_FIELDS, "count(id) as id from (select", SELECT_FIELDS, "from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{toId} order by id desc) tt group by conversation_id order by id desc " +
                    "limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(1) from ", TABLE_NAME, " where conversation_id=#{conversationId} and user_id=#{userId} and has_read=0"})
    int getConversationUnReadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(1) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId}"})
    int getConversationTotalCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME, " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);
}
