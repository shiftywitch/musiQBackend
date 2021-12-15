package no.birkeland.musiq.domain;

import lombok.*;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class QueueItem {
    @NonNull
    private Long queueItemId;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String url;
    private Long queueId;

    public QueueItemDto toDto() {
        return new QueueItemDto(queueItemId, title, description, url, queueId);
    }

    public static QueueItem toQueueItem(QueueItemDto queueItemDto) {
        return new QueueItem(
                queueItemDto.getQueueItemId(),
                queueItemDto.getTitle(),
                queueItemDto.getDescription(),
                queueItemDto.getUrl());
    }
}
