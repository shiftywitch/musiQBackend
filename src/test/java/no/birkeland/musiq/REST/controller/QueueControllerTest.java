package no.birkeland.musiq.REST.controller;

import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.QueueItem;
import no.birkeland.musiq.service.QueueService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QueueControllerTest {

    @MockBean
    public QueueService queueService;

    @LocalServerPort
    public int port;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    void getQueueById() {
        QueueDto expected = new QueueDto(
                1L, "TestTitle", new ArrayList<>()
        );

        Mockito.when(queueService.getQueueById(1L)).thenReturn(expected);
        ResponseEntity<QueueDto> re = testRestTemplate.getForEntity("/api/queues/1", QueueDto.class);

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody().toString()).isEqualTo(expected.toString());
    }

    @Test
    void getQueues() {
        List<QueueDto> expectedQueues = Arrays.asList(
                new QueueDto(1L, "TestTitle", new ArrayList<>()),
                new QueueDto(2L, "TestTitle", new ArrayList<>())
        );

        Mockito.when(queueService.getQueues()).thenReturn(expectedQueues);
        ResponseEntity<QueueDto[]> re = testRestTemplate.getForEntity("/api/queues", QueueDto[].class);

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.toString(Arrays.stream(re.getBody()).toArray())).isEqualTo(expectedQueues.toString());
    }

    @Test
    void createQueue() {
        QueueDto newQueue = new QueueDto("TestQueue");

        HttpEntity<QueueDto> request = new HttpEntity<>(newQueue);
        ArgumentCaptor<QueueDto> captor = ArgumentCaptor.forClass(QueueDto.class);
        Mockito.when(queueService.createQueue(Mockito.any(QueueDto.class))).thenReturn(1L);

        ResponseEntity<Long> re = testRestTemplate.postForEntity("/api/queues/newQueue", request, Long.class);

        Mockito.verify(queueService, Mockito.times(1)).createQueue(captor.capture());

        QueueDto capturedQueueDto = captor.getValue();

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(re.getBody()).isEqualTo(1L);
        assertThat(capturedQueueDto.toString()).isEqualTo(newQueue.toString());
    }

    @Test
    void deleteQueueById() {
        Mockito.when(queueService.deleteQueueById(Mockito.anyLong())).thenReturn(true);

        RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create("/api/queues/delete/1"));
        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(requestEntity, Boolean.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(responseEntity.getBody());
    }

    @Test
    void addItemToQueue() {
        QueueItemDto newQueueItem = new QueueItemDto("Title", "Description", "http://youtube.com", 1L);

        HttpEntity<QueueItemDto> request = new HttpEntity<>(newQueueItem);
        ArgumentCaptor<QueueItemDto> captor = ArgumentCaptor.forClass(QueueItemDto.class);
        Mockito.when(queueService.addItemToQueue(Mockito.any(QueueItemDto.class))).thenReturn(1L);

        ResponseEntity<Long> re = testRestTemplate.postForEntity("/api/queues/addItemToQueue", request, Long.class);

        Mockito.verify(queueService, Mockito.times(1)).addItemToQueue(captor.capture());

        QueueItemDto capturedQueueItem = captor.getValue();

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(re.getBody()).isEqualTo(1L);
        assertThat(capturedQueueItem.toString()).isEqualTo(newQueueItem.toString());
    }

    @Test
    void deleteItemFromQueue() {
        Mockito.when(queueService.deleteItemFromQueue(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE, URI.create("/api/queues/1/deleteItem_1"));
        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(requestEntity, Boolean.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertTrue(responseEntity.getBody());

    }
}