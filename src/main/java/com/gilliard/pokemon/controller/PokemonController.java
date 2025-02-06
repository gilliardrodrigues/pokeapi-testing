package com.gilliard.pokemon.controller;

import com.gilliard.pokemon.dto.PokemonHighlightDTO;
import com.gilliard.pokemon.dto.PokemonResponseDTO;
import com.gilliard.pokemon.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {
    private final PokemonService pokemonService;

    public PokemonController() {
        this.pokemonService = new PokemonService();
    }

    @GetMapping
    public ResponseEntity<PokemonResponseDTO<String>> getPokemons(@RequestParam(value = "query", required = false) String query,
                                                                  @RequestParam(value = "sort", required = false) String sort) {
        PokemonResponseDTO<String> responseDTO = pokemonService.getPokemons(query, sort);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/highlight")
    public ResponseEntity<PokemonResponseDTO<PokemonHighlightDTO>> getPokemonsWithHighlight(@RequestParam(value = "query", required = false) String query,
                                                                                            @RequestParam(value = "sort", required = false) String sort) {
        PokemonResponseDTO<PokemonHighlightDTO> responseDTO = pokemonService.getPokemonsWithHighlight(query, sort);
        return ResponseEntity.ok(responseDTO);
    }
}
