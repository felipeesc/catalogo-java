package com.projeto.catalogo.service;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.domain.dto.PlayersDTO;
import com.projeto.catalogo.repository.PlayersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayersRepository repository;

    public Flux<Players> listAll(){
        return repository.findAll();
    }

    public Mono<Players> findById(int id) {
        return repository.findById(id)
                .switchIfEmpty(monoResponseNotFoundException(id))
                .log();
    }

    public Mono<Players> save(PlayersDTO playersDTO) {
        Players players = new Players();
        BeanUtils.copyProperties(playersDTO, players);
        return repository.save(players);
    }

    public Flux<Players> saveList(List<PlayersDTO> playersDTO) {
        List<Players> players = new ArrayList<>();
        BeanUtils.copyProperties(playersDTO, players);
        return repository.saveAll(players);
    }

    public Mono<Void> update(PlayersDTO playersDTO){
        Players players = new Players();
        BeanUtils.copyProperties(playersDTO, players);
        return findById(players.getId())
                .map(pocFound -> players.withId(pocFound.getId()))
                .flatMap(repository::save)
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> delete(int id) {
        return findById(id)
                .flatMap(repository::delete);
    }

    public <T> Mono<T> monoResponseNotFoundException(int id) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "object not found with id " + id));
    }


}
