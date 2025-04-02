package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.fixtures.ConstructeurDePokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
*Premier vide
* Premier null
* Second vide
* Seconde null
* Memes
* Bechoue si erreure avec l'api pour le premier/second
 */

class BagarreTest {

    @Test
    void devrait_lancer_une_erreur_si_premier_pokemon_est_vide() {
        Bagarre bagarre = new Bagarre();

        Throwable thrown = catchThrowable(() -> bagarre.demarrer("", "pikachu"));

        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lancer_une_erreur_si_premier_pokemon_est_null() {
        Bagarre bagarre = new Bagarre();

        Throwable thrown = catchThrowable(() -> bagarre.demarrer(null, "pikachu"));

        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void devrait_lancer_une_erreur_si_second_pokemon_est_vide() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("pikachu", ""));
        System.out.println(thrown.getMessage());
        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }


    @Test
    void devrait_lancer_une_erreur_si_second_pokemon_est_null() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("pikachu", null));
        System.out.println(thrown.getMessage());
        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurPokemonNonRenseigne.class)
                .hasMessage("Le second pokemon n'est pas renseigne");
    }


    @Test
    void devrait_lancer_une_erreur_si_les_deux_pokemons_sont_identiques() {
        // Given
        Bagarre bagarre = new Bagarre();

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer("pikachu", "Pikachu"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ErreurMemePokemon.class);
    }

    @Test
    void quand_premier_gagnant_devrait_retourner_le_premier() {
        // Given
        PokeBuildApi fausseApi = mock(PokeBuildApi.class);
        Bagarre bagarre = new Bagarre(fausseApi);
        //Pokemon pikachu = new ConstructeurDePokemon().avecAttaque(50).avecDefense(20).construire();
        //Pokemon salameche = new ConstructeurDePokemon().avecAttaque(30).avecDefense(15).construire();

        Pokemon pikachu = new Pokemon("pikachu", "url1", new Stats(50, 20));
        Pokemon salameche = new Pokemon("salameche", "url2", new Stats(30, 15));

        when(fausseApi.recupererParNom("pikachu"))
                .thenReturn(CompletableFuture.completedFuture(pikachu));
        when(fausseApi.recupererParNom("salameche"))
                .thenReturn(CompletableFuture.completedFuture(salameche));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("pikachu", "salameche");

        // Then
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies(pokemon -> {
                    assertThat(pokemon.getNom()).isEqualTo("pikachu");
                    assertThat(pokemon.getUrlImage()).isEqualTo("url1"); //verif tous les autres attributs
                });
    }



    @Test
    void echoue_si_erreure_api_premier() {
        // Given
        PokeBuildApi fausseApi = mock(PokeBuildApi.class);
        Bagarre bagarre = new Bagarre(fausseApi);
        //Pokemon pikachu = new ConstructeurDePokemon().avecAttaque(50).avecDefense(20).construire();
        //Pokemon salameche = new ConstructeurDePokemon().avecAttaque(30).avecDefense(15).construire();

        Pokemon pikachu = new Pokemon("pikachu", "url1", new Stats(50, 20));
        Pokemon salameche = new Pokemon("salameche", "url2", new Stats(30, 15));

        when(fausseApi.recupererParNom("pikachu"))
                .thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("pikachu")));
        when(fausseApi.recupererParNom("salameche"))
                .thenReturn(CompletableFuture.completedFuture(salameche));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("pikachu", "salameche");

        // Then
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ExecutionException.class)
                .havingCause()
                .isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'pikachu'");

    }

    void echoue_si_erreure_api_second() {
        // Given
        PokeBuildApi fausseApi = mock(PokeBuildApi.class);
        Bagarre bagarre = new Bagarre(fausseApi);
        // Pokemon pikachu = new ConstructeurDePokemon().avecAttaque(50).avecDefense(20).construire();
        // Pokemon salameche = new ConstructeurDePokemon().avecAttaque(30).avecDefense(15).construire();

        Pokemon pikachu = new Pokemon("pikachu", "url1", new Stats(50, 20));
        Pokemon salameche = new Pokemon("salameche", "url2", new Stats(30, 15));

        when(fausseApi.recupererParNom("pikachu"))
                .thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("pikachu")));
        when(fausseApi.recupererParNom("salameche"))
                .thenReturn(CompletableFuture.completedFuture(salameche));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer("salameche", "pikachu");

        // Then
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ExecutionException.class)
                .havingCause()
                .isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'pikachu'");

    }
}