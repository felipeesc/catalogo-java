package com.projeto.catalogo.service;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.domain.dto.PlayersDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class CalculateService {

    public Flux<Players> calculate(List<PlayersDTO> playersDTO) {
        Random random = new Random();
        playersDTO.indexOf(random.nextInt());

        int sizeArray = playersDTO.size();

        var nextRandString = rand.Next(0, sizeArray - 1);

        playersDTO.stream()
                        .filter(x -> Collections.sort(x.getName()))
        Collections.sort(playersDTO, Comparator.comparingInt(PlayersDTO::getId));
        System.out.println(playersDTO);
        return null;
    }



}
