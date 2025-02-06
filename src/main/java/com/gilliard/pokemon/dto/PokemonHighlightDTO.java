package com.gilliard.pokemon.dto;

public class PokemonHighlightDTO {
    private final String name;
    private final String highlight;

    public PokemonHighlightDTO(String name, String highlight) {
        this.name = name;
        this.highlight = highlight;
    }

    public String getName() {
        return name;
    }

    public String getHighlight() {
        return highlight;
    }
}

