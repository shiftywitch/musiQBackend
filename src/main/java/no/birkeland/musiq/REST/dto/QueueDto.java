package no.birkeland.musiq.REST.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QueueDto {
    private Long queueId;
    @NonNull
    private String title;
    private List<QueueItemDto> queueItems;
}
