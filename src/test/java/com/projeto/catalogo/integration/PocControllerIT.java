package com.projeto.catalogo.integration;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.repository.PlayersRepository;
import com.projeto.catalogo.service.PlayersService;
import com.projeto.catalogo.util.PlayersCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import(PlayersService.class)
public class PocControllerIT {

    @MockBean
    private PlayersRepository repository;

    @Autowired
    private WebTestClient testClient;

    private final Players players = PlayersCreator.createValidPoc();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(repository.findAll())
                .thenReturn(Flux.just(players));

        BDDMockito.when(repository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(players));

        BDDMockito.when(repository.save(PlayersCreator.createPocToBeSaved()))
                .thenReturn(Mono.just(players));


//        BDDMockito.when(pocRepository.delete(ArgumentMatchers.anyInt()))
//                .thenReturn(Mono.empty());
//
//        BDDMockito.when(pocRepository.update(PocCreator.createValidPocDTO()))
//                .thenReturn(Mono.empty());
    }

    @Test
    void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    @DisplayName("findAll returns a flux of poc")
    void findAll_returnFluxOfPoc_whenSuccessful() {
        testClient
                .get()
                .uri("/v1")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Players.class)
                .hasSize(1)
                .contains(players);
//                .expectStatus().isOk()
//                .expectBody()
//                .jsonPath("$.[0].id").isEqualTo(poc.getId())
//                .jsonPath("$.[0].name").isEqualTo(poc.getName());
    }

}
