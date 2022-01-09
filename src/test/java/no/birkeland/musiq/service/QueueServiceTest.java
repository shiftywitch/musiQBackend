package no.birkeland.musiq.service;

import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.repository.QueueRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    static Queue testQueue;
    static Queue testQueue2;
    static Queue testQueue3;

    static QueueDto testQueueDto;

    static QueueItemDto testQueueItemDto;

    //Mocking authentication.
    @BeforeAll
    static void setupMockAuthentication() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("1234");
    }

    @BeforeEach
    void setup() {
        testQueue = new Queue("1234", 1L, "TestQueue", new ArrayList<>());
        testQueue2 = new Queue("1234", 2L, "TestQueue", new ArrayList<>());
        testQueue3 = new Queue("1234", 3L, "TestQueue", new ArrayList<>());

        testQueueDto = new QueueDto("testQueueDto");

        testQueueItemDto = new QueueItemDto("testQueueItemDto", "Description", "url", 1L);
    }

    @Test
    void getQueueById() {
        Queue toReturn = testQueue;
        Mockito.when(queueRepository.getQueueById(1L, "1234")).thenReturn(toReturn);
        QueueDto queueDto = queueService.getQueueById(1L);

        assertThat(Queue.toQueue(queueDto).toString()).isEqualTo(toReturn.toString());
    }

    @Test
    void getQueues() {
        List<Queue> queues = List.of(testQueue, testQueue2, testQueue3);

        Mockito.when(queueRepository.getQueuesByUserId("1234")).thenReturn(queues);
        List<QueueDto> queuesFromService = queueService.getQueuesByUserId();

        assertThat(queuesFromService.size()).isEqualTo(3);
        assertThat(queuesFromService.stream().map(Queue::toQueue).collect(Collectors.toList()).toString())
                .isEqualTo(queues.toString());
    }

    @Test
    void createQueue() {

        Mockito.when(queueRepository.createQueue(testQueueDto)).thenReturn(1L);

        Long queueId = queueService.createQueue(testQueueDto);

        assertThat(queueId).isEqualTo(1L);
    }

    @Test
    void deleteQueueById() {
        Mockito.when(queueRepository.deleteQueueById(1L, "1234")).thenReturn(true);

        Boolean deleted = queueService.deleteQueueById(1L);
        Boolean deleted2 = queueService.deleteQueueById(2L);

        assertTrue(deleted);
        assertFalse(deleted2);
    }

    @Test
    void addItemToQueue() {
        Mockito.when(queueRepository.addItemToQueue(testQueueItemDto, "1234")).thenReturn(1L);

        Long queueItemId = queueService.addItemToQueue(testQueueItemDto);

        assertThat(queueItemId).isEqualTo(1L);
        assertThat(queueItemId).isNotEqualTo(2L);
    }

    @Test
    void deleteItemFromQueue() {
        Mockito.when(queueRepository.getQueueById(1L, SecurityContextHolder.getContext().getAuthentication().getName())).thenReturn(testQueue);
        Mockito.when(queueRepository.deleteItemFromQueue(1L, 1L)).thenReturn(true);

        Boolean deleted = queueService.deleteItemFromQueue(1L, 1L);
        Boolean deleted2 = queueService.deleteItemFromQueue(2L, 1L);

        assertTrue(deleted);
        assertFalse(deleted2);
    }
}