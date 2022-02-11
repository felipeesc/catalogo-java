package com.projeto.catalogo.controller;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.service.PlayersService;
import com.projeto.catalogo.util.PlayersCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class PocControllerTest {

    @InjectMocks
    private PlayersController playersController;

    @Mock
    private PlayersService playersService;

    private final Players players = PlayersCreator.createValidPoc();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setUp() {
        BDDMockito.when(playersService.listAll())
                .thenReturn(Flux.just(players));

        BDDMockito.when(playersService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(players));

        BDDMockito.when(playersService.save(PlayersCreator.createPocDTOToBeSaved()))
                .thenReturn(Mono.just(players));


        BDDMockito.when(playersService.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        BDDMockito.when(playersService.update(PlayersCreator.createValidPocDTO()))
                .thenReturn(Mono.empty());
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
        StepVerifier.create(playersController.listAll())
                .expectSubscription()
                .expectNext(players)
                .verifyComplete();
    }

    @Test
    @DisplayName("save create an poc when successful")
    void save_createPoc_whenSuccessful() {
        StepVerifier.create(playersController.save(PlayersCreator.createPocDTOToBeSaved()))
                .expectSubscription()
                .expectNext(players)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes the poc when successful")
    void delete_Poc_whenSuccessful() {
        StepVerifier.create(playersController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update save update poc and returns empty mono when successful")
    void update_SaveUpdatePoc_whenSuccessful() {
        StepVerifier.create(playersController.update(1, PlayersCreator.createValidPocDTO()))
                .expectSubscription()
                .verifyComplete();
    }
}