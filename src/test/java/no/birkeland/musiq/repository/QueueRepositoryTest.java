package no.birkeland.musiq.repository;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.CreateQueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.domain.QueueItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QueueRepositoryTest {

    @Autowired
    QueueRepository queueRepository;

    @Test
    void getQueueById() {
        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        Queue queue = queueRepository.getQueueById(id);

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(queue.getQueueId()).isEqualTo(id);

        //Delete testData
        queueRepository.deleteQueueById(id);
    }

    @Test
    void getQueues() {
        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        Long id2 = queueRepository.createQueue(new CreateQueueDto("TestQueue2"));
        Long id3 = queueRepository.createQueue(new CreateQueueDto("TestQueue3"));

        List<Queue> queues = queueRepository.getQueues();

        assertThat(queues.size()).isGreaterThan(2);
        assertThat(queues).isNotEmpty();

        queueRepository.deleteQueueById(id);
        queueRepository.deleteQueueById(id2);
        queueRepository.deleteQueueById(id3);
    }

    @Test
    void createQueue() {
        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        Queue queue = queueRepository.getQueueById(id);

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(queue.getQueueId()).isEqualTo(id);
        assertThat(queue.getQueueItems()).isEmpty();

        //Delete testData
        queueRepository.deleteQueueById(id);
    }

    @Test
    void deleteQueueById() {
        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        Queue queue = queueRepository.getQueueById(id);
        Boolean result = queueRepository.deleteQueueById(id);

        assertThat(queue.getTitle()).isEqualTo("TestQueue");
        assertThat(result).isTrue();

        Throwable exception = assertThrows(RuntimeException.class, () -> queueRepository.getQueueById(id));
        assertEquals("Queue with id: " + id + ", does not exists.", exception.getMessage());
    }

    @Test
    void getQueueitemsById() {
        QueueItemDto queueItemDto = new QueueItemDto(
                "TestTitle",
                "Description",
                "https://www.youtube.com/watch?v=XXcKzltuaSk"
        );
        QueueItemDto queueItemDto2 = new QueueItemDto(
                "TestTitle2",
                "Description2",
                "https://www.youtube.com/watch?v=XXcKzltuaSk"
        );

        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        queueRepository.addItemToQueue(id, queueItemDto);
        queueRepository.addItemToQueue(id, queueItemDto2);

        List<QueueItem> queueItems = queueRepository.getQueueItemsById(id);

        assertThat(queueItems.size()).isEqualTo(2);
        assertThat(queueItems.get(1).getDescription()).isEqualTo("Description2");

        queueRepository.deleteQueueById(id);
    }

    @Test
    void addItemToQueue() {

        QueueItemDto queueItemDto = new QueueItemDto(
                "TestTitle",
                "Description",
                "https://www.youtube.com/watch?v=XXcKzltuaSk"
        );

        Long id = queueRepository.createQueue(new CreateQueueDto("TestQueue"));
        Long queueItemId = queueRepository.addItemToQueue(id, queueItemDto);

        Queue queue = queueRepository.getQueueById(id);

        assertThat(queue.getQueueItems().get(0).toDto().getDescription()).isEqualTo(queueItemDto.getDescription());
        assertThat(queue.getQueueItems().get(0).toDto().getUrl()).isEqualTo(queueItemDto.getUrl());
        assertThat(queue.getQueueItems().get(0).toDto().getTitle()).isEqualTo(queueItemDto.getTitle());
        assertThat(queue.getQueueItems().get(0).toDto().getQueueId()).isEqualTo(queueItemDto.getQueueId());

        queueRepository.deleteQueueById(id);
    }
}