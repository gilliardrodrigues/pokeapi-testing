package com.gilliard.pokemon.utils;

import com.gilliard.pokemon.model.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Classe utilitária que implementa o algoritmo MergeSort para ordenação de listas de Pokémon.<br>
 *
 * - Complexidade de tempo: O(n log n)<br>
 * - Estratégia: divisão e conquista (divide recursivamente a lista em metades, ordena cada parte e depois combina de forma ordenada).<br>
 * - Justificativa da complexidade: o número de divisões necessárias para recursivamente reduzir a lista em n listas de
 * um elemento cada é log n. A mesclagem envolve percorrer os n elementos de cada nível da árvore,
 * então temos n * log n = O(n log n).<br>
 * <br>
 * Se estivesse usando alguma biblioteca de sorting do Java, bem como Comparators, talvez fosse uma boa usar o padrão Strategy e criar duas estratégias: uma para ordenar por nome e outra pelo tamanho.
 *
 */
public class MergeSortUtils {

    /**
     * Implementação genérica do MergeSort para ordenar listas de Pokémon com base em um critério de comparação.
     *
     * @param list Lista a ser ordenada.
     * @param compare Função de comparação que recebe dois Pokémon e retorna um inteiro.
     * @return Lista ordenada.
     */
    public static List<Pokemon> mergeSort(List<Pokemon> list, BiFunction<Pokemon, Pokemon, Integer> compare) {
        if (list.size() <= 1)
            return list; // Caso base
        // Divide a lista em duas:
        SplitResult split = splitList(list);
        // Ordena recursivamente as metades:
        List<Pokemon> left = mergeSort(split.left, compare);
        List<Pokemon> right = mergeSort(split.right, compare);
        // Mescla as duas listas ordenadas pelo critério solicitado:
        return merge(left, right, compare);
    }

    /**
     * Divide uma lista em duas partes.
     *
     * @param list Lista original a ser dividida.
     * @return Objeto SplitResult contendo as listas esquerda e direita.
     */
    private static SplitResult splitList(List<Pokemon> list) {
        int middle = list.size() / 2;
        return new SplitResult(
                new ArrayList<>(list.subList(0, middle)),
                new ArrayList<>(list.subList(middle, list.size()))
        );
    }

    /**
     * Mescla duas listas ordenadas com base na função de comparação.
     *
     * @param left Lista esquerda ordenada.
     * @param right Lista direita ordenada.
     * @param compare Função de comparação.
     * @return Lista mesclada e ordenada.
     */
    private static List<Pokemon> merge(List<Pokemon> left, List<Pokemon> right, BiFunction<Pokemon, Pokemon, Integer> compare) {
        List<Pokemon> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            // Se compare.apply(left, right) retorna valor negativo ou zero, left vem antes
            if (compare.apply(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }
        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    /**
     * Classe auxiliar para armazenar as listas esquerda e direita.
     */
    private static class SplitResult {
        List<Pokemon> left;
        List<Pokemon> right;

        SplitResult(List<Pokemon> left, List<Pokemon> right) {
            this.left = left;
            this.right = right;
        }
    }
}
