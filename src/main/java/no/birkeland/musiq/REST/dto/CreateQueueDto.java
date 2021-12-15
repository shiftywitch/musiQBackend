package no.birkeland.musiq.REST.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateQueueDto {
    private String title;
}
