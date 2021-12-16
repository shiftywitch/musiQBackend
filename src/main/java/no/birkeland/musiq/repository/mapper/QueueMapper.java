package no.birkeland.musiq.repository.mapper;

import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.domain.Queue;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QueueMapper implements RowMapper<Queue> {

    @Override
    public Queue mapRow(ResultSet rs, int rowNum) throws SQLException {
        Queue queue = new Queue();
        queue.setQueueId(rs.getLong("id"));
        queue.setTitle(rs.getString("title"));

        return queue;
    }

    public static Map<String, String> getParamMap(QueueDto queueDto) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("title", queueDto.getTitle());

        return paramMap;
    }
}
