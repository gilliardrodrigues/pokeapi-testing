package com.gilliard.pokemon.service;

import com.gilliard.pokemon.model.Pokemon;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.gilliard.pokemon.utils.ApiUtils.convertToMapList;

@Service
public class PokemonService {
    private final RestTemplate restTemplate;


    public PokemonService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Pokemon> getFilteredPokemons(String query) {
        List<Pokemon> filteredPokemons = getAllPokemons();

        if (query == null || query.trim().isEmpty()) {
            return filteredPokemons;
        }
        String caseInsensitiveQuery = query.trim().toLowerCase();
        return filteredPokemons.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(caseInsensitiveQuery))
                .toList();
    }

    public List<Pokemon> getAllPokemons() {
        List<Pokemon> pokemons = new ArrayList<>();
        // Inicia com a primeira página, limitada a 100 resultados:
        String nextPageUrl = "https://pokeapi.co/api/v2/pokemon?limit=100";

        while (nextPageUrl != null) {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    nextPageUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> response = responseEntity.getBody();

            if (response == null || !response.containsKey("results")) {
                throw new RuntimeException("Erro: resposta da API é nula!");
            }
            List<Map<String, String>> results = convertToMapList(response.get("results"));

            for (Map<String, String> pokemonData : results) {
                Pokemon pokemon = getPokemon(pokemonData);
                pokemons.add(pokemon);
            }
            nextPageUrl = (String) response.get("next"); // Passa para a próxima página.
        }
        return pokemons;
    }

    /**
     * Converte um mapa contendo dados de um Pokémon em um objeto {@code Pokemon}.
     *
     * <p>O mapa deve conter as chaves "name" (nome do Pokémon) e "url" (URL da API com detalhes do Pokémon).</p>
     *
     * @param pokemonData Um {@code Map<String, String>} contendo as informações do Pokémon.
     * @return Um objeto {@code Pokemon} com ID, nome e URL preenchidos.
     */
    private static Pokemon getPokemon(Map<String, String> pokemonData) {
        String pokemonName = pokemonData.get("name");
        String pokemonUrl = pokemonData.get("url");
        String pokemonId = getIdFromPokemonUrl(pokemonUrl);

        return new Pokemon(pokemonId, pokemonName, pokemonUrl);
    }

    /**
     * Extrai o ID do Pokémon a partir da URL da API.
     *
     * <p>Exemplo de entrada:
     * <pre>
     *     getIdFromPokemonUrl("https://pokeapi.co/api/v2/pokemon/25/")
     * </pre>
     * Saída: {@code "25"}</p>
     *
     * @param pokemonUrl A URL do Pokémon na API.
     * @return O ID do Pokémon como {@code String}.
     */
    private static String getIdFromPokemonUrl(String pokemonUrl) {
        String[] urlParts = pokemonUrl.split("/");
        return urlParts[urlParts.length - 1];
    }
}
