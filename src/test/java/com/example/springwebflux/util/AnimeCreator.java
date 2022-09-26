package com.example.springwebflux.util;

import com.example.springwebflux.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Brotherhood")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .id(1)
                .name("Naruto")
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .id(1)
                .name("Cross")
                .build();
    }


}
