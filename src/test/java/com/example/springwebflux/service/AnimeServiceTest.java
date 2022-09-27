package com.example.springwebflux.service;

import com.example.springwebflux.domain.Anime;
import com.example.springwebflux.repository.AnimeRepository;
import com.example.springwebflux.util.AnimeCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeEach
    public void setup(){
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));
        BDDMockito.when(animeRepositoryMock.findById(BDDMockito.anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createAnimeToBeSaved()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(animeRepositoryMock.delete(BDDMockito.any()))
                .thenReturn(Mono.empty());

//        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatcher.anyInt()))
//                .thenReturn(Mono.just(anime));

    }

    @Test
    @DisplayName("listAll returns a flux of anime")
    public void findAll_ReturnsFluxOfAnime_WhenSuccessful(){
        StepVerifier.create(animeService.findAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete()
        ;
    }

    @Test
    @DisplayName("findById returns a mono of anime if exists")
    public void findById_ReturnsMonoOfAnime_WhenSuccessful(){
        StepVerifier.create(animeService.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete()
        ;
    }

    @Test
    @DisplayName("findById returns a mono error when anime doesn't exists")
    public void findById_ReturnsMonoError_WhenEmptyMonoIsReturned(){
        BDDMockito.when(animeRepositoryMock.findById(BDDMockito.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify()
        ;
    }

    @Test
    @DisplayName("save creates an anime when successfull")
    public void save_CreatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        StepVerifier.create(animeService.save(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete()
        ;
    }

    @Test
    @DisplayName("delete removes anime when successful")
    public void delete_RemovesAnime_WhenSuccessful(){
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .verifyComplete()
        ;
    }

    @Test
    @DisplayName("delete returns mono error when anime is not found")
    public void delete_ReturnsMonoError_WhenSuccessful(){
        BDDMockito.when(animeRepositoryMock.findById(BDDMockito.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(animeService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }


    @Test
    @DisplayName("updates returns empty when successfull")
    public void updated_Returns_Empty_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createValidUpdatedAnime();
        BDDMockito.when(animeRepositoryMock.save(AnimeCreator.createValidUpdatedAnime()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.update(animeToBeSaved))
                .expectSubscription()
                .verifyComplete()
        ;
    }

    @Test
    @DisplayName("updates returns mono error when cant find anime")
    public void updated_Returns_MonoError_WhenUnsuccessful(){
        Anime animeToBeSaved = AnimeCreator.createValidUpdatedAnime();
        BDDMockito.when(animeRepositoryMock.findById(BDDMockito.anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.update(animeToBeSaved))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify()
        ;
    }











}