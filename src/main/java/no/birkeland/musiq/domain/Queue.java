package no.birkeland.musiq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.birkeland.musiq.REST.dto.QueueDto;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Queue {

    private String ownerId;
    private Long queueId;
    private String title;
    private List<QueueItem> queueItems;

    public QueueDto toDto() {
        return new QueueDto(
                ownerId,
                queueId,
                title,
                queueItems.stream().map(QueueItem::toDto).collect(Collectors.toList()));
    }

    public static Queue toQueue(QueueDto queueDto) {
        return new Queue(
                queueDto.getOwnerId(),
                queueDto.getQueueId(),
                queueDto.getTitle(),
                queueDto.getQueueItems().stream().map(QueueItem::toQueueItem).collect(Collectors.toList()));
    }
}
