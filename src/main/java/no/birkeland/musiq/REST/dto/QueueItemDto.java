package no.birkeland.musiq.REST.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QueueItemDto {
    private Long queueItemId;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String url;
    private Long queueId;
}
