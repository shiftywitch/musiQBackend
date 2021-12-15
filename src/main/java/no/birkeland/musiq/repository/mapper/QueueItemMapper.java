package no.birkeland.musiq.repository.mapper;

import no.birkeland.musiq.REST.dto.CreateQueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.QueueItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QueueItemMapper implements RowMapper<QueueItem> {

    @Override
    public QueueItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        QueueItem queueItem = new QueueItem();
        queueItem.setQueueItemId(rs.getLong("id"));
        queueItem.setTitle(rs.getString("title"));
        queueItem.setDescription(rs.getString("description"));
        queueItem.setUrl(rs.getString("url"));
        queueItem.setQueueItemId(rs.getLong("queueid"));

        return queueItem;
    }

    public static Map<String, Object> getParamMap(QueueItemDto queueItemDto, Long queueId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", queueItemDto.getTitle());
        paramMap.put("description", queueItemDto.getDescription());
        paramMap.put("url", queueItemDto.getUrl());
        paramMap.put("queueid", queueId);

        return paramMap;
    }
}
