package com.example.springwebflux.service;

import com.example.springwebflux.domain.Anime;
import com.example.springwebflux.repository.AnimeRepository;
import com.example.springwebflux.util.AnimeCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeRepository.findAll())
                .thenReturn(Flux.just(anime));
    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    public void listAll_ReturnsFluxOfAnime_WhenSuccessful(){
        StepVerifier.create(animeService.findAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete()
        ;
    }




}