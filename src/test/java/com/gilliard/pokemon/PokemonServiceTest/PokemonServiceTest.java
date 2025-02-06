package com.gilliard.pokemon.PokemonServiceTest;

import com.gilliard.pokemon.cache.PokemonCache;
import com.gilliard.pokemon.dto.PokemonHighlightDTO;
import com.gilliard.pokemon.dto.PokemonResponseDTO;
import com.gilliard.pokemon.model.Pokemon;
import com.gilliard.pokemon.service.PokemonService;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class PokemonServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PokemonService pokemonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pokemonService = new PokemonService();

        PokemonCache pokemonCache = PokemonCache.getInstance();
        pokemonCache.clearCache();
    }

    @Test
    void testGetPokemons_WithQuery() {
        // Setup do mock
        List<Pokemon> mockPokemons = List.of(new Pokemon("25", "Pikachu", "url"));
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método
        String query = "Pik";
        String sort = "name";
        PokemonResponseDTO<String> response = pokemonService.getPokemons(query, sort);

        // Verificando se o resultado é o esperado
        assertEquals(1, response.getResult().size());
        assertEquals("Pikachu", response.getResult().getFirst());
    }

    @Test
    void testGetPokemons_WithoutQuery() {
        // Setup do mock
        List<Pokemon> mockPokemons = Arrays.asList(new Pokemon("25", "Pikachu", "url"), new Pokemon("4", "Charmander", "url"));
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método
        PokemonResponseDTO<String> response = pokemonService.getPokemons(null, null);

        // Verificando se todos os Pokémons foram retornados
        assertEquals(2, response.getResult().size());
        assertTrue(response.getResult().contains("Pikachu"));
        assertTrue(response.getResult().contains("Charmander"));
    }

    @Test
    void testGetPokemonsWithHighlight_WithQuery() {
        // Setup do mock
        List<Pokemon> mockPokemons = List.of(new Pokemon("25", "Pikachu", "url"));
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método
        String query = "Pik";
        String sort = "name";
        PokemonResponseDTO<PokemonHighlightDTO> response = pokemonService.getPokemonsWithHighlight(query, sort);

        // Verificando se o Pokémon foi retornado com highlight
        assertEquals(1, response.getResult().size());
        assertEquals("<pre>Pik</pre>achu", response.getResult().getFirst().getHighlight());
    }

    @Test
    void testGetPokemonsWithHighlight_WithoutQuery() {
        // Setup do mock
        List<Pokemon> mockPokemons = List.of(new Pokemon("25", "Pikachu", "url"));
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método
        PokemonResponseDTO<PokemonHighlightDTO> response = pokemonService.getPokemonsWithHighlight(null, null);

        // Verificando se o Pokémon foi retornado sem highlight
        assertEquals(1, response.getResult().size());
        assertEquals("Pikachu", response.getResult().getFirst().getHighlight());
    }

    @Test
    void testGetPokemons_WithSortByLength() {
        // Setup do mock
        List<Pokemon> mockPokemons = Arrays.asList(
                new Pokemon("1", "Bulbasaur", "url"),
                new Pokemon("25", "Pikachu", "url"),
                new Pokemon("4", "Charmander", "url")
        );
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método com ordenação por tamanho
        String query = null;
        String sort = "length";
        PokemonResponseDTO<String> response = pokemonService.getPokemons(query, sort);

        // Verificando se a ordenação está funcionando corretamente (deve ser: Charmander, Pikachu, Bulbasaur)
        List<String> sortedPokemons = response.getResult();
        assertEquals(3, sortedPokemons.size());
        assertEquals("Pikachu", sortedPokemons.get(0));
        assertEquals("Bulbasaur", sortedPokemons.get(1));
        assertEquals("Charmander", sortedPokemons.get(2));
    }

    @Test
    void testGetPokemonsWithHighlight_WithSortByLength() {
        // Setup do mock
        List<Pokemon> mockPokemons = Arrays.asList(
                new Pokemon("1", "Bulbasaur", "url"),
                new Pokemon("4", "Charmander", "url"),
                new Pokemon("25","Pikachu", "url")
        );
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executando o método com ordenação por tamanho
        String query = null;
        String sort = "length";
        PokemonResponseDTO<PokemonHighlightDTO> response = pokemonService.getPokemonsWithHighlight(query, sort);

        // Verificando se a ordenação está funcionando corretamente (deve ser: Charmander, Pikachu, Bulbasaur)
        List<PokemonHighlightDTO> sortedPokemons = response.getResult();
        assertEquals(3, sortedPokemons.size());
        assertEquals("Pikachu", sortedPokemons.get(0).getHighlight());
        assertEquals("Bulbasaur", sortedPokemons.get(1).getHighlight());
        assertEquals("Charmander", sortedPokemons.get(2).getHighlight());
    }

    @Test
    void testGetPokemons_WithSortByName() {
        // Setup do mock
        List<Pokemon> mockPokemons = Arrays.asList(
                new Pokemon("25","Pikachu", "url"),
                new Pokemon("1", "Bulbasaur", "url"),
                new Pokemon("4", "Charmander", "url")
        );
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executar o método com ordenação por nome
        String query = null;
        String sort = "name";
        PokemonResponseDTO<String> response = pokemonService.getPokemons(query, sort);

        // Verificar se a ordenação está funcionando corretamente (deve ser: Bulbasaur, Charmander, Pikachu)
        List<String> sortedPokemons = response.getResult();
        assertEquals(3, sortedPokemons.size());
        assertEquals("Bulbasaur", sortedPokemons.get(0));
        assertEquals("Charmander", sortedPokemons.get(1));
        assertEquals("Pikachu", sortedPokemons.get(2));
    }

    // Teste de ordenação por "name" com highlight
    @Test
    void testGetPokemonsWithHighlight_WithSortByName() {
        List<Pokemon> mockPokemons = Arrays.asList(
                new Pokemon("25","Pikachu", "url"),
                new Pokemon("1", "Bulbasaur", "url"),
                new Pokemon("4", "Charmander", "url")
        );
        PokemonCache pokemonCache = PokemonCache.getInstance();
        mockPokemons.forEach(pokemonCache::addPokemon);

        // Executar o método com ordenação por nome
        String query = null;
        String sort = "name";
        PokemonResponseDTO<PokemonHighlightDTO> response = pokemonService.getPokemonsWithHighlight(query, sort);

        // Verificar se a ordenação está funcionando corretamente (deve ser: Bulbasaur, Charmander, Pikachu)
        List<PokemonHighlightDTO> sortedPokemons = response.getResult();
        assertEquals(3, sortedPokemons.size());
        assertEquals("Bulbasaur", sortedPokemons.get(0).getHighlight());
        assertEquals("Charmander", sortedPokemons.get(1).getHighlight());
        assertEquals("Pikachu", sortedPokemons.get(2).getHighlight());
    }
}
