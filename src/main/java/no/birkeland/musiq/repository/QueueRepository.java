package no.birkeland.musiq.repository;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.CreateQueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.domain.QueueItem;
import no.birkeland.musiq.repository.mapper.QueueItemMapper;
import no.birkeland.musiq.repository.mapper.QueueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class QueueRepository  {

    private final JdbcTemplate jdbcTemplate;

    //Everything with queues!

    public Queue getQueueById(Long id) {
        String sql = "SELECT * FROM queue WHERE id = ?";
        List<Queue> queues = jdbcTemplate.query(sql, new QueueMapper(), id);

        if (queues.isEmpty()) {
            throw new RuntimeException("Queue with id: " + id + ", does not exists.");
        } else {
            queues.get(0).setQueueItems(getQueueItemsById(id));
            return queues.get(0);
        }
    }

    public List<Queue> getQueues() {
        String sql = "SELECT * FROM queue";
        List<Queue> queues = jdbcTemplate.query(sql, new QueueMapper());
        queues.forEach(queue -> queue.setQueueItems(getQueueItemsById(queue.getQueueId())));

        return queues;
    }

    public Long createQueue(CreateQueueDto createQueueDto) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("queue").usingGeneratedKeyColumns("id");
        return simpleJdbcInsert
                .executeAndReturnKey(QueueMapper.getParamMap(createQueueDto)).longValue();
    }

    public Boolean deleteQueueById(Long id) {
        String sql = "DELETE FROM queue WHERE id = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    //Everything with queueItems!

    public List<QueueItem> getQueueItemsById(Long id) {
        String sql = "SELECT * FROM queueitem WHERE queueid = ?";
        return jdbcTemplate.query(sql, new QueueItemMapper(), id);
    }

    public Long addItemToQueue(Long queueId, QueueItemDto queueItemDto) {
        //throws if queue is not found
        getQueueById(queueId);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("queueitem").usingGeneratedKeyColumns("id");

        return simpleJdbcInsert
                .executeAndReturnKey(QueueItemMapper.getParamMap(queueItemDto, queueId)).longValue();
    }

    public Boolean deleteItemFromQueue(Long queueId, Long itemId) {
        String sql = "DELETE FROM queueitem WHERE id = ? AND queueId = ?";
        return jdbcTemplate.update(sql, queueId, itemId) == 1;
    }
}
