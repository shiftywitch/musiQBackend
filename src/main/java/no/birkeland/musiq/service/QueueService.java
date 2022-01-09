package no.birkeland.musiq.service;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.repository.QueueRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QueueService {

    final QueueRepository queueRepository;

    private String getLoggedInUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /** Service methods for Queues **/

    public QueueDto getQueueById(Long id) { return queueRepository.getQueueById(id, getLoggedInUserId()).toDto(); }

    public List<QueueDto> getQueuesByUserId() {
        return queueRepository.getQueuesByUserId(getLoggedInUserId()).stream().map(Queue::toDto).collect(Collectors.toList());
    }

    public Long createQueue(QueueDto queueDto) {
        queueDto.setOwnerId(getLoggedInUserId());
        return queueRepository.createQueue(queueDto);
    }

    public Boolean deleteQueueById(Long id) {
        return queueRepository.deleteQueueById(id, getLoggedInUserId());
    }

    /** Service methods for QueueItems **/

    public Long addItemToQueue(QueueItemDto queueItemDto) {
        return queueRepository.addItemToQueue(queueItemDto, getLoggedInUserId());
    }

    public Boolean deleteItemFromQueue(Long queueId, Long itemId) {
        //Throws exception if the queue is not found, or if it is not owned by the signed in user.
        queueRepository.getQueueById(queueId, getLoggedInUserId());
        return queueRepository.deleteItemFromQueue(queueId, itemId);
    }
}
