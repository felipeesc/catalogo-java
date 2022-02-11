package com.projeto.catalogo.util;

import com.projeto.catalogo.domain.Players;
import com.projeto.catalogo.domain.dto.PlayersDTO;

public class PlayersCreator {

    public static PlayersDTO createPocDTOToBeSaved(){
        return PlayersDTO.builder()
                .name("data")
                .build();
    }

    public static Players createPocToBeSaved(){
        return Players.builder()
                .name("data")
                .build();
    }

    public static Players createValidPoc(){
        return Players.builder()
                .id(1)
                .name("data")
                .build();
    }

    public static PlayersDTO createValidPocDTO(){
        return PlayersDTO.builder()
                .id(1)
                .name("data")
                .build();
    }

    public static Players createvalidUpdatePoc(){
        return Players.builder()
                .id(1)
                .name("data 2")
                .build();
    }
}
