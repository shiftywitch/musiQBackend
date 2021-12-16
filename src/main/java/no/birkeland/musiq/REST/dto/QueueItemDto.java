package no.birkeland.musiq.REST.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class QueueItemDto {
    private Long queueItemId;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String url;
    @NonNull
    private Long queueId;
}
