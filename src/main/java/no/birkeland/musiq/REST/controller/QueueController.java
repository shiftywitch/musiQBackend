package no.birkeland.musiq.REST.controller;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.service.QueueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin("*")
@RequestMapping("/api/queues")
@AllArgsConstructor
public class QueueController {

    final QueueService queueService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getQueueById(@PathVariable Long id) {
        QueueDto response = queueService.getQueueById(id);
        return new ResponseEntity<QueueDto>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getQueues() {
        List<QueueDto> response = queueService.getQueues();
        return new ResponseEntity<List<QueueDto>>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/newQueue")
    public ResponseEntity<?> createQueue(@RequestBody QueueDto queueDto) {
        Long response = queueService.createQueue(queueDto);
        return new ResponseEntity<Long>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteQueueById(@PathVariable Long id) {
        Boolean response = queueService.deleteQueueById(id);
        return new ResponseEntity<Boolean>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/addItemToQueue")
    public ResponseEntity<?> addItemToQueue(@RequestBody QueueItemDto queueItemDto) {
        Long response = queueService.addItemToQueue(queueItemDto);
        return new ResponseEntity<Long>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{queueId}/deleteItem_{itemId}")
    public ResponseEntity<?> deleteItemFromQueue(@PathVariable Long queueId,
                                               @PathVariable Long itemId) {
        Boolean response = queueService.deleteItemFromQueue(queueId, itemId);
        return new ResponseEntity<Boolean>(response, HttpStatus.OK);
    }

}
