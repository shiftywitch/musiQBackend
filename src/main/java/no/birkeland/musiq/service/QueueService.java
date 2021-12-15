package no.birkeland.musiq.service;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.CreateQueueDto;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueueService {

    final QueueRepository queueRepository;

    public QueueDto getQueueById(Long id) { return queueRepository.getQueueById(id).toDto(); }


    public List<QueueDto> getQueues() {
        return queueRepository.getQueues().stream().map(Queue::toDto).collect(Collectors.toList());
    }

    public Long createQueue(CreateQueueDto createQueueDto) {
        return queueRepository.createQueue(createQueueDto);
    }

    public Boolean deleteQueueById(Long id) {
        return queueRepository.deleteQueueById(id);
    }

    public Long addItemToQueue(Long queueId, QueueItemDto queueItemDto) {
        return queueRepository.addItemToQueue(queueId, queueItemDto);
    }

    public Boolean deleteItemFromQueue(Long queueId, Long itemId) {
        return queueRepository.deleteItemFromQueue(queueId, itemId);
    }
}
