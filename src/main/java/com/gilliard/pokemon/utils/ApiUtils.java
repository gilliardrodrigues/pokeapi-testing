package com.gilliard.pokemon.utils;

import java.util.*;
import java.util.stream.Collectors;

public class ApiUtils {

    /**
     * Converte um objeto genérico em uma lista de mapas {@code List<Map<String, String>>}, garantindo segurança de tipo.
     *
     * <p>Se o objeto não for uma lista de mapas, retorna uma lista vazia. A conversão garante que todas as chaves e valores sejam do tipo {@code String}.</p>
     *
     * @param rawData O objeto a ser convertido, geralmente obtido de uma API REST.
     * @return Uma lista de mapas {@code List<Map<String, String>>}, onde cada chave e valor são Strings.
     */
    public static List<Map<String, String>> convertToMapList(Object rawData) {
        if (!(rawData instanceof List<?> rawList)) {
            return Collections.emptyList(); // Retorna uma lista vazia se o objeto não for uma lista
        }

        return rawList.stream()
                .filter(item -> item instanceof Map)
                .map(item -> ((Map<?, ?>) item).entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> String.valueOf(e.getKey()),
                                e -> String.valueOf(e.getValue())
                        ))
                )
                .collect(Collectors.toList());
    }
}

