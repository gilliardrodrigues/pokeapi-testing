package com.gilliard.pokemon.dto;

import java.util.List;

public class PokemonResponseDTO {
    private List<String> result;

    public PokemonResponseDTO(List<String> result) {
        this.result = result;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
