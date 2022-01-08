package no.birkeland.musiq.REST.controller;

import com.google.gson.Gson;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.service.QueueService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class QueueControllerTest {

    @MockBean
    public QueueService queueService;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public Gson gson;


    @Test
    void getQueueById2() throws Exception {
        QueueDto expected = new QueueDto(1L, "TestTitle", new ArrayList<>());

        Mockito.when(queueService.getQueueById(1L)).thenReturn(expected);

        mockMvc.perform(get("/api/queues/1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(gson.toJson(expected))))
        ;
    }


    @Test
    void getQueues() throws Exception {
        List<QueueDto> expectedQueues = Arrays.asList(
                new QueueDto(1L, "TestTitle", new ArrayList<>()),
                new QueueDto(2L, "TestTitle", new ArrayList<>())
        );

        Mockito.when(queueService.getQueues()).thenReturn(expectedQueues);

        mockMvc.perform(get("/api/queues")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(gson.toJson(expectedQueues))))
        ;
    }


    @Test
    void createQueue() throws Exception {
        QueueDto newQueue = new QueueDto("TestQueue");
        ArgumentCaptor<QueueDto> captor = ArgumentCaptor.forClass(QueueDto.class);

        Mockito.when(queueService.createQueue(Mockito.any(QueueDto.class))).thenReturn(1L);

        mockMvc.perform(post("/api/queues/newQueue").with(jwt()).content(gson.toJson(newQueue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("1")));

        Mockito.verify(queueService, Mockito.times(1)).createQueue(captor.capture());
        QueueDto capturedQueueDto = captor.getValue();

        assertThat(capturedQueueDto.toString()).isEqualTo(newQueue.toString());
    }


    @Test
    void deleteQueueById() throws Exception {
        Mockito.when(queueService.deleteQueueById(Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/queues/delete/{id}", 1).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }


    @Test
    void addItemToQueue() throws Exception {
        QueueItemDto newQueueItem = new QueueItemDto("Title", "Description", "http://youtube.com", 1L);
        ArgumentCaptor<QueueItemDto> captor = ArgumentCaptor.forClass(QueueItemDto.class);

        Mockito.when(queueService.addItemToQueue(Mockito.any(QueueItemDto.class))).thenReturn(1L);

        mockMvc.perform(post("/api/queues/addItemToQueue").with(jwt()).content(gson.toJson(newQueueItem))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("1")));

        Mockito.verify(queueService, Mockito.times(1)).addItemToQueue(captor.capture());
        QueueItemDto capturedQueueItem = captor.getValue();

        assertThat(capturedQueueItem.toString()).isEqualTo(newQueueItem.toString());
    }

    @Test
    void deleteItemFromQueue() throws Exception {
        Mockito.when(queueService.deleteItemFromQueue(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/queues/{queueId}/deleteItem_{itemId}", 1, 1).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }
}