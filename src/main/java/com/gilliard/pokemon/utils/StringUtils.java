package com.gilliard.pokemon.utils;

public class StringUtils {

    /**
     * Destaca a primeira ocorrência de uma substring dentro de um texto, envolvendo-a com a tag pre.
     *
     * @param substring A substring a ser destacada no texto.
     * @param text O texto no qual a busca será realizada.
     * @return O texto modificado com a primeira ocorrência da substring destacada, 
     * ou o texto original se a substring não for encontrada ou o texto/substring for nulo.
     */
    public static String highlightFirstOccurrence(String text, String substring) {
        if (substring == null || text == null || substring.isEmpty() || text.isEmpty())
            return text;

        int index = text.toLowerCase().indexOf(substring.toLowerCase());
        if (index == -1)
            return text;

        StringBuilder result = new StringBuilder();
        result.append(text, 0, index)
                .append("<pre>")
                .append(text, index, index + substring.length())
                .append("</pre>")
                .append(text.substring(index + substring.length()));
        return result.toString();
    }

}
