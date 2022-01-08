package no.birkeland.musiq.service;

import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.repository.QueueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    @Test
    void getQueueById() {
        Queue toReturn = new Queue(1L, "TestQueue", new ArrayList<>());
        Mockito.when(queueRepository.getQueueById(1L)).thenReturn(toReturn);
        QueueDto queueDto = queueService.getQueueById(1L);

        assertThat(Queue.toQueue(queueDto).toString()).isEqualTo(toReturn.toString());
    }

    @Test
    void getQueues() {
        Queue queue1 = new Queue(1L, "TestQueue", new ArrayList<>());
        Queue queue2 = new Queue(2L, "TestQueue", new ArrayList<>());
        Queue queue3 = new Queue(3L, "TestQueue", new ArrayList<>());
        List<Queue> queues = List.of(queue1, queue2, queue3);

        Mockito.when(queueRepository.getQueues()).thenReturn(queues);
        List<QueueDto> queuesFromService = queueService.getQueues();

        assertThat(queuesFromService.size()).isEqualTo(3);
        assertThat(queuesFromService.stream().map(Queue::toQueue).collect(Collectors.toList()).toString())
                .isEqualTo(queues.toString());
    }

    @Test
    void createQueue() {
        QueueDto queueDto = new QueueDto("TestTitle");

        Mockito.when(queueRepository.createQueue(queueDto)).thenReturn(1L);

        Long queueId = queueService.createQueue(queueDto);

        assertThat(queueId).isEqualTo(1L);
    }

    @Test
    void deleteQueueById() {
        Mockito.when(queueRepository.deleteQueueById(1L)).thenReturn(true);

        Boolean deleted = queueService.deleteQueueById(1L);
        Boolean deleted2 = queueService.deleteQueueById(2L);

        assertTrue(deleted);
        assertFalse(deleted2);
    }

    @Test
    void addItemToQueue() {
        QueueItemDto queueItemDto = new QueueItemDto("Title", "Description", "url", 1L);

        Mockito.when(queueRepository.addItemToQueue(queueItemDto)).thenReturn(1L);

        Long queueItemId = queueService.addItemToQueue(queueItemDto);

        assertThat(queueItemId).isEqualTo(1L);
        assertThat(queueItemId).isNotEqualTo(2L);
    }

    @Test
    void deleteItemFromQueue() {
        Mockito.when(queueRepository.deleteItemFromQueue(1L, 1L)).thenReturn(true);

        Boolean deleted = queueService.deleteItemFromQueue(1L, 1L);
        Boolean deleted2 = queueService.deleteItemFromQueue(2L, 1L);

        assertTrue(deleted);
        assertFalse(deleted2);
    }
}