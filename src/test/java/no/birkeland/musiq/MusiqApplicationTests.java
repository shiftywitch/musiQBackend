package no.birkeland.musiq;

import lombok.AllArgsConstructor;
import no.birkeland.musiq.REST.dto.QueueItemDto;
import no.birkeland.musiq.domain.Queue;
import no.birkeland.musiq.domain.QueueItem;
import no.birkeland.musiq.repository.QueueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AllArgsConstructor
class MusiqApplicationTests {

    @Test
    void contextLoads() { }

}
