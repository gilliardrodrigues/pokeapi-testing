package com.gilliard.pokemon.dto;

import java.util.List;

public class PokemonResponseDTO<T> {
    private List<T> result;

    public PokemonResponseDTO(List<T> result) {
        this.result = result;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}

