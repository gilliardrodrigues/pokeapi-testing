package com.gilliard.pokemon.controller;

import com.gilliard.pokemon.dto.PokemonResponseDTO;
import com.gilliard.pokemon.model.Pokemon;
import com.gilliard.pokemon.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController() {
        this.pokemonService = new PokemonService();
    }

    @GetMapping
    public ResponseEntity<PokemonResponseDTO> getPokemons(@RequestParam(value = "query", required = false) String query,
                                                          @RequestParam(value = "sort", required = false) String sort) {
        PokemonResponseDTO responseDTO = pokemonService.getPokemons(query, sort);
        return ResponseEntity.ok(responseDTO);
    }
}
