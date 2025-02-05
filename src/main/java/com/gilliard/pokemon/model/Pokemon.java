package com.gilliard.pokemon.model;

public class Pokemon {
    private final String id;
    private final String name;
    private final String url;

    public Pokemon(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Pokemon{id='" + id + "', name='" + name + "'}";
    }


}
