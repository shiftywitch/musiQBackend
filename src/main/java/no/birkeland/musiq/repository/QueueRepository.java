package no.birkeland.musiq.repository;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.domain.QueueItem;
import no.birkeland.musiq.repository.mapper.QueueItemMapper;
import no.birkeland.musiq.repository.mapper.QueueMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class QueueRepository  {

    private final JdbcTemplate jdbcTemplate;

    //Everything with queues!

    public Queue getQueueById(Long id, String userId) {
        String sql = (userId != null)
                ? "SELECT * FROM queue WHERE id = ? and ownerid = ?"
                : "SELECT * FROM queue WHERE id = ?";
        List<Queue> queues = (userId != null)
                ? jdbcTemplate.query(sql, new QueueMapper(), id, userId)
                : jdbcTemplate.query(sql, new QueueMapper(), id);

        if (queues.isEmpty()) {
            throw new RuntimeException("Queue with id: " + id + " and owner: " + userId +", does not exists.");
        } else {
            queues.get(0).setQueueItems(getQueueItemsById(id));
            return queues.get(0);
        }
    }

    public List<Queue> getQueuesByUserId(String userId) {
        String sql = "SELECT * FROM queue where ownerId = ?";
        List<Queue> queues = jdbcTemplate.query(sql, new QueueMapper(), userId);
        queues.forEach(queue -> queue.setQueueItems(getQueueItemsById(queue.getQueueId())));

        return queues;
    }

    public Long createQueue(QueueDto queueDto) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("queue").usingGeneratedKeyColumns("id");
        return simpleJdbcInsert
                .executeAndReturnKey(QueueMapper.getParamMap(queueDto)).longValue();
    }

    public Boolean deleteQueueById(Long id, String userId) {
        String sql = (userId != null)
                ? "DELETE FROM queue WHERE id = ? AND ownerid = ?"
                : "DELETE FROM queue WHERE id = ?";
        return (userId != null)
                ? jdbcTemplate.update(sql, id, userId) == 1
                : jdbcTemplate.update(sql, id) == 1;
    }

    //Everything with queueItems!

    public List<QueueItem> getQueueItemsById(Long id) {
        String sql = "SELECT * FROM queueitem WHERE queueid = ?";
        return jdbcTemplate.query(sql, new QueueItemMapper(), id);
    }

    public Long addItemToQueue(QueueItemDto queueItemDto, String userId) {
        //throws if queue is not found
        getQueueById(queueItemDto.getQueueId(), userId);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("queueitem").usingGeneratedKeyColumns("id");

        return simpleJdbcInsert
                .executeAndReturnKey(QueueItemMapper.getParamMap(queueItemDto)).longValue();
    }

    public Boolean deleteItemFromQueue(Long queueId, Long itemId) {

        String sql = "DELETE FROM queueitem WHERE id = ? AND queueId = ?";
        return jdbcTemplate.update(sql, itemId, queueId) == 1;
    }
}
