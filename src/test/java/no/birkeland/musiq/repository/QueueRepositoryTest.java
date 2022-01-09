package no.birkeland.musiq.repository;

import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.domain.QueueItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueueRepositoryTest {

    @Autowired
    QueueRepository queueRepository;

    static QueueDto queue1;
    static QueueDto queue2;
    static QueueDto queue3;

    static QueueItemDto queueItemDto;
    static QueueItemDto queueItemDto2;

    @BeforeEach
     void setup() {
        queue1 = new QueueDto("TestQueue");
        queue2 = new QueueDto("TestQueue2");
        queue3 = new QueueDto("TestQueue3");

        queue1.setOwnerId("1234");
        queue2.setOwnerId("1234");
        queue3.setOwnerId("1234");

        queueItemDto = new QueueItemDto(

                "TestTitle",
                "Description",
                "https://www.youtube.com/watch?v=XXcKzltuaSk",
                1L
        );
        queueItemDto2 = new QueueItemDto(
                "TestTitle2",
                "Description2",
                "https://www.youtube.com/watch?v=XXcKzltuaSk",
                1L
        );
    }

    @Test
    void getQueueById() {
        Long id = queueRepository.createQueue(queue1);
        Queue queue = queueRepository.getQueueById(id, queue1.getOwnerId());

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(queue.getQueueId()).isEqualTo(id);

        //Delete testData
        queueRepository.deleteQueueById(id, null);
    }

    @Test
    void getQueues() {


        Long id = queueRepository.createQueue(queue1);
        Long id2 = queueRepository.createQueue(queue2);
        Long id3 = queueRepository.createQueue(queue3);

        List<Queue> queues = queueRepository.getQueuesByUserId("1234");

        assertThat(queues.size()).isGreaterThan(2);
        assertThat(queues).isNotEmpty();

        queueRepository.deleteQueueById(id, null);
        queueRepository.deleteQueueById(id2, null);
        queueRepository.deleteQueueById(id3, null);
    }

    @Test
    void createQueue() {
        Long id = queueRepository.createQueue(queue1);
        Queue queue = queueRepository.getQueueById(id, queue1.getOwnerId());

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(queue.getQueueId()).isEqualTo(id);
        assertThat(queue.getQueueItems()).isEmpty();

        //Delete testData
        queueRepository.deleteQueueById(id, null);
    }

    @Test
    void deleteQueueById() {
        Long id = queueRepository.createQueue(queue1);
        Queue queue = queueRepository.getQueueById(id, queue1.getOwnerId());
        Boolean result = queueRepository.deleteQueueById(id, null);

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(result).isTrue();

        Throwable exception = assertThrows(RuntimeException.class, () -> queueRepository.getQueueById(id, queue1.getOwnerId()));
        assertEquals("Queue with id: " + id + " and owner: " + queue.getOwnerId() + ", does not exists.", exception.getMessage());
    }

    @Test
    void getQueueitemsById() {
        Long queueId = queueRepository.createQueue(queue1);

        queueItemDto.setQueueId(queueId);
        queueItemDto2.setQueueId(queueId);

        queueRepository.addItemToQueue(queueItemDto, queue1.getOwnerId());
        queueRepository.addItemToQueue(queueItemDto2, queue1.getOwnerId());

        List<QueueItem> queueItems = queueRepository.getQueueItemsById(queueId);

        assertThat(queueItems.size()).isEqualTo(2);
        assertThat(queueItems.get(1).getDescription()).isEqualTo("Description2");

        queueRepository.deleteQueueById(queueId, null);
    }

    @Test
    void addItemToQueue() {

        Long queueId = queueRepository.createQueue(queue1);
        queueItemDto.setQueueId(queueId);

        Long queueItemId = queueRepository.addItemToQueue(queueItemDto, queue1.getOwnerId());
        Queue queue = queueRepository.getQueueById(queueId, queue1.getOwnerId());

        assertThat(queue.getQueueItems().get(0).toDto().getDescription()).isEqualTo(queueItemDto.getDescription());
        assertThat(queue.getQueueItems().get(0).toDto().getUrl()).isEqualTo(queueItemDto.getUrl());
        assertThat(queue.getQueueItems().get(0).toDto().getTitle()).isEqualTo(queueItemDto.getTitle());
        assertThat(queue.getQueueItems().get(0).toDto().getQueueId()).isEqualTo(queueItemDto.getQueueId());

        queueRepository.deleteQueueById(queueId, null);
    }

    @Test
    void deleteItemFromQueue() {
        Long queueId = queueRepository.createQueue(queue1);
        queueItemDto.setQueueId(queueId);

        Long queueItemId = queueRepository.addItemToQueue(queueItemDto, queue1.getOwnerId());

        Boolean deleted = queueRepository.deleteItemFromQueue(queueId, queueItemId);

        assertTrue(deleted);

        queueRepository.deleteQueueById(queueId, null);
    }
}