package com.gilliard.pokemon.cache;

import com.gilliard.pokemon.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class PokemonCache {
    private static PokemonCache instance;
    /* Se não fôssemos verificar parte do nome dos pokémons no serviço, mas sim buscar pelo nome diretamente,
     seria mais vantajoso utilizar um Set aqui pela vantagem de look-up eficiente (O(1)), mas como o contains utilizado
     lá será O(n) sobre o tamanho de cada nome, List foi escolhido.
     */
    private final List<Pokemon> cache = new ArrayList<>();

    // Constructor privado para garantir que a instância só seja criada internamente:
    private PokemonCache() {}

    // Método para acessar a instância única do cache:
    public static synchronized PokemonCache getInstance() {
        if (instance == null) {
            instance = new PokemonCache();
        }
        return instance;
    }

    // Métodos de manipulação do cache:
    public List<Pokemon> getCache() {
        return cache;
    }
    public void addPokemon(Pokemon pokemon) {
        cache.add(pokemon);
    }
    public void clearCache() {
        cache.clear();
    }
}

