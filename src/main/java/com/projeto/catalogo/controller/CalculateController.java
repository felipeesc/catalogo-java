package com.projeto.catalogo.controller;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.domain.dto.PlayersDTO;
import com.projeto.catalogo.service.CalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CalculateController {

    @Autowired
    private CalculateService calculateService;

    @PostMapping(path = "/calculate")
    public Flux<Players> calculateList(@Valid @RequestBody List<PlayersDTO> playersDTO) {
        return calculateService.calculate(playersDTO);
    }
}
