package com.projeto.catalogo.service;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.repository.PlayersRepository;
import com.projeto.catalogo.util.PlayersCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class PocServiceTest {

    @InjectMocks
    private PlayersService playersService;

    @Mock
    private PlayersRepository repository;

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


        BDDMockito.when(repository.delete(ArgumentMatchers.any(Players.class)))
                .thenReturn(Mono.empty());

        BDDMockito.when(repository.save(PlayersCreator.createValidPoc()))
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
        StepVerifier.create(playersService.listAll())
                .expectSubscription()
                .expectNext(players)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById return Mono with poc whe it exists")
    void findById_returnMonoPoc_whenSuccessful() {
        StepVerifier.create(playersService.findById(1))
                .expectSubscription()
                .expectNext(players)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById return Mono error when poc does not exist")
    void findById_returnMonoPoc_whenEmptyMonoIsReturn() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(playersService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("save create an poc when successful")
    void save_createPoc_whenSuccessful() {
        StepVerifier.create(playersService.save(PlayersCreator.createPocDTOToBeSaved()))
                .expectSubscription()
                .expectNext(players)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes the poc when successful")
    void delete_Poc_whenSuccessful() {
        StepVerifier.create(playersService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete removes Mono error when poc does not exist")
    void delete_ReturnMonoErro_whenEmptyMonoIsReturned() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(playersService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("update save update poc and returns empty mono when successful")
    void update_SaveUpdatePoc_whenSuccessful() {
        StepVerifier.create(playersService.update(PlayersCreator.createValidPocDTO()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update return Mono error when anime does exist")
    void update_ReturnMonoErro_whenEmptyMonoIsReturned() {
        BDDMockito.when(repository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(playersService.update(PlayersCreator.createValidPocDTO()))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}