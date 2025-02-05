package com.gilliard.pokemon.model;

import java.util.function.BiFunction;

/**
 * Enum para definir os tipos de ordenação da lista de Pokémon.
 */
public enum SortType {

    /**
     * Ordena por nome em ordem alfabética crescente (A-Z), ignorando caracteres especiais.
     */
    NAME((p1, p2) -> cleanString(p1.getName()).compareTo(cleanString(p2.getName()))),

    /**
     * Ordena de forma crescente pelo tamanho do nome.
     */
    LENGTH((p1, p2) -> Integer.compare(p1.getName().length(), p2.getName().length()));

    private final BiFunction<Pokemon, Pokemon, Integer> evaluator;

    SortType(BiFunction<Pokemon, Pokemon, Integer> evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * Retorna a função de comparação associada ao tipo de ordenação.
     *
     * @return BiFunction que compara dois Pokémon.
     */
    public BiFunction<Pokemon, Pokemon, Integer> getEvaluator() {
        return evaluator;
    }

    /**
     * Converte uma string em um SortType válido.
     *
     * @param value String do tipo de ordenação.
     * @return SortType correspondente.
     * @throws IllegalArgumentException se o tipo de ordenação for inválido.
     */
    public static SortType fromString(String value) {
        for (SortType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de ordenação inválido: " + value);
    }

    /**
     * Remove caracteres especiais da string para evitar problemas de ordenação.
     *
     * @param name Nome do Pokémon.
     * @return Nome sem caracteres especiais.
     */
    private static String cleanString(String name) {
        return name.replaceAll("[^a-zA-Z0-9]", " "); // Remove hífens e outros caracteres especiais
    }
}


