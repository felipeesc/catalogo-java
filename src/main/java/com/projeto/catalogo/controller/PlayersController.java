package com.projeto.catalogo.controller;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.domain.dto.PlayersDTO;
import com.projeto.catalogo.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PlayersController {

    @Autowired
    private PlayersService playersService;

    @GetMapping
    public Flux<Players> listAll() {
        return playersService.listAll();
    }

    @PostMapping
    public Mono<Players> save(@Valid @RequestBody PlayersDTO playersDTO) {
        return playersService.save(playersDTO);
    }

    @PostMapping(path = "/list")
    public Flux<Players> saveList(@Valid @RequestBody List<PlayersDTO> playersDTO) {
        return playersService.saveList(playersDTO);
    }

    @PutMapping(path = "{id}")
    public Mono<Void> update(@PathVariable int id, @Valid @RequestBody PlayersDTO playersDTO) {
        return playersService.update(playersDTO);
    }

    @DeleteMapping(path = "{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return playersService.delete(id);
    }

}
