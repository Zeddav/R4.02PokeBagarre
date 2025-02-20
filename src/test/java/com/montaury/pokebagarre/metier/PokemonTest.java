/*
 * Test pour pokebagarre

Scénario 1 : Le Pokémon 1 a une meilleure attaque que le Pokémon 2.
Entrée : Pokémon 1 avec attaque 10 et défense 5, Pokémon 2 avec attaque 9 et défense 5.
Sortie attendue : Le Pokémon 1 gagne, car il a une meilleure attaque.

Scénario 2 : Le Pokémon 1 a une moins bonne attaque que le Pokémon 2.
Entrée : Pokémon 1 avec attaque 9 et défense 5, Pokémon 2 avec attaque 10 et défense 5.
Sortie attendue : Le Pokémon 2 gagne, car il a une meilleure attaque.

Scénario 3 : Les deux Pokémon ont la même attaque, mais le Pokémon 1 a une meilleure défense.
Entrée : Pokémon 1 avec attaque 10 et défense 6, Pokémon 2 avec attaque 10 et défense 5.
Sortie attendue : Le Pokémon 1 gagne, car il a une meilleure défense.

Scénario 4 : Les deux Pokémon ont la même attaque, mais le Pokémon 1 a une moins bonne défense.
Entrée : Pokémon 1 avec attaque 10 et défense 5, Pokémon 2 avec attaque 10 et défense 6.
Sortie attendue : Le Pokémon 2 gagne, car il a une meilleure défense.

Scénario 5 : Les deux Pokémon ont la même attaque et la même défense.
Entrée : Pokémon 1 avec attaque 10 et défense 6, Pokémon 2 avec attaque 10 et défense 6.
Sortie attendue : Le Pokémon 1 gagne, car il est renseigné en premier (priorité au premier Pokémon).

Scénario 6 : Les deux Pokémon ont la même attaque et la même défense, mais l'ordre des paramètres est inversé.
Entrée : Pokémon 1 avec attaque 10 et défense 6, Pokémon 2 avec attaque 10 et défense 6 (l'ordre des paramètres est inversé).
Sortie attendue : Le Pokémon 2 gagne, car il est renseigné en premier (priorité au deuxième Pokémon).
 */

package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.fixtures.ConstructeurDePokemon;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTests {
    @Test
    void force_sup(){
        // GIVEN
        Pokemon p1 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(5).construire();
        Pokemon p2 = new ConstructeurDePokemon().avecAttaque(9).avecDefense(5).construire();

        // WHEN
        boolean resultat = p1.estVainqueurContre(p2);

        // THEN
        assertThat(resultat).isTrue();
    }

    @Test
    void force_inf(){
        // GIVEN
        Pokemon p1 = new ConstructeurDePokemon().avecAttaque(9).avecDefense(5).construire();
        Pokemon p2 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(5).construire();

        // WHEN
        boolean resultat = p1.estVainqueurContre(p2);

        // THEN
       // assertEquals(resultat, false);
        assertThat(resultat).isFalse();
    }

    @Test
    void def_sup(){
        // GIVEN
        Pokemon p1 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(6).construire();
        Pokemon p2 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(5).construire();

        // WHEN
        boolean resultat = p1.estVainqueurContre(p2);

        // THEN
        assertThat(resultat).isTrue();

    }

    @Test
    void def_inf(){
        // GIVEN
        Pokemon p1 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(6).construire();
        Pokemon p2 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(5).construire();

        // WHEN
        boolean resultat = p1.estVainqueurContre(p2);

        // THEN
        assertThat(resultat).isTrue();

    }

    @Test
    void stat_meme(){
        // GIVEN
        Pokemon p1 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(6).construire();
        Pokemon p2 = new ConstructeurDePokemon().avecAttaque(10).avecDefense(6).construire();

        // WHEN
        boolean resultat1 = p1.estVainqueurContre(p2);
        boolean resultat2 = p2.estVainqueurContre(p1);

        // THEN
        assertThat(resultat1).isTrue();

        assertThat(resultat2).isTrue();

    }
}