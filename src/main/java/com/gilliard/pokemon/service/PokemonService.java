package com.gilliard.pokemon.service;

import com.gilliard.pokemon.dto.PokemonHighlightDTO;
import com.gilliard.pokemon.dto.PokemonResponseDTO;
import com.gilliard.pokemon.model.Pokemon;
import com.gilliard.pokemon.model.SortType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.gilliard.pokemon.utils.ApiUtils.convertToMapList;
import static com.gilliard.pokemon.utils.MergeSortUtils.mergeSort;
import static com.gilliard.pokemon.utils.StringUtils.highlightFirstOccurrence;

@Service
public class PokemonService {
    private final RestTemplate restTemplate;


    public PokemonService() {
        this.restTemplate = new RestTemplate();
    }

    public PokemonResponseDTO<String> getPokemons(String query, String sort) {
        List<Pokemon> pokemons = getAllPokemons();

        if (!isParamEmpty(query)) {
            String caseInsensitiveQuery = query.trim().toLowerCase();
            pokemons = pokemons.stream()
                    .filter(pokemon -> isValidResult(pokemon.getName(), caseInsensitiveQuery))
                    .toList();
        }
        SortType sortType = SortType.fromString(sort);
        pokemons = mergeSort(pokemons, sortType.getEvaluator());
        return convertPokemonListToDTOResponse(pokemons);
    }

    public PokemonResponseDTO<PokemonHighlightDTO> getPokemonsWithHighlight(String query, String sort) {
        List<Pokemon> pokemons = getAllPokemons();

        SortType sortType = SortType.fromString(sort);
        pokemons = mergeSort(pokemons, sortType.getEvaluator());
        List<PokemonHighlightDTO> results = new ArrayList<>();

        if (isParamEmpty(query)) {
            for (Pokemon pokemon : pokemons) {
                results.add(new PokemonHighlightDTO(pokemon.getName(), pokemon.getName()));
            }
        } else {
            String caseInsensitiveQuery = query.trim().toLowerCase();
            String highlight;
            for (Pokemon pokemon : pokemons) {
                String pokemonName = pokemon.getName();
                if (isValidResult(pokemonName, caseInsensitiveQuery)) {
                    highlight = highlightFirstOccurrence(pokemonName, caseInsensitiveQuery);
                    results.add(new PokemonHighlightDTO(pokemonName, highlight));
                }
            }
        }
        return new PokemonResponseDTO<>(results);
    }

    /**
     * Verifica se o nome do Pokémon contém a string de busca (ignorando maiúsculas e minúsculas).
     *
     * @param pokemonName O nome do Pokémon a ser verificado.
     * @param caseInsensitiveQuery A string de busca, que será comparada ao nome do Pokémon.
     * @return Retorna true se o nome do Pokémon contém a string de busca, ignorando maiúsculas e minúsculas; caso contrário, retorna false.
     */
    private Boolean isValidResult(String pokemonName, String caseInsensitiveQuery) {
        return pokemonName.toLowerCase().contains(caseInsensitiveQuery);
    }

    /**
     * Converte uma lista de objetos Pokémon para um DTO (Data Transfer Object) que contém os nomes dos Pokémons.
     *
     * @param pokemons A lista de objetos Pokémon que será convertida.
     * @return Retorna um objeto {@link PokemonResponseDTO} contendo uma lista de nomes dos Pokémons.
     */
    private static PokemonResponseDTO<String> convertPokemonListToDTOResponse(List<Pokemon> pokemons) {
        List<String> pokemonNames = pokemons.stream()
                .map(Pokemon::getName)
                .toList();
        return new PokemonResponseDTO<>(pokemonNames);
    }

    /**
     * Verifica se o parâmetro fornecido está vazio ou é nulo.
     *
     * @param param O parâmetro a ser verificado.
     * @return Retorna true se o parâmetro for nulo ou contiver apenas espaços em branco; caso contrário, retorna false.
     */
    private static Boolean isParamEmpty(String param) {
        return param == null || param.trim().isEmpty();
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
