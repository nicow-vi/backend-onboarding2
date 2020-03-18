package com.virtualidentity.onboarding.pokemons.rest;

import com.virtualidentity.onboarding.common.rest.controller.BaseController;
import com.virtualidentity.onboarding.generated.PokemonsApi;
import com.virtualidentity.onboarding.generated.model.AuthorList;
import com.virtualidentity.onboarding.generated.model.Pokemon;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class PokemonController extends BaseController implements PokemonsApi {

  @Override
  public ResponseEntity<List<Pokemon>> getPokemons() {
    Pokemon pokemon1 = new Pokemon().name("Pikachu").element("electric");
    Pokemon pokemon2 = new Pokemon().name("Bisasam").element(("plant"));
    Pokemon pokemon3 = new Pokemon().name("Charizard").element("fire");
    List<Pokemon> pokemonList = new ArrayList<>();
    pokemonList.add(pokemon1);
    pokemonList.add(pokemon2);
    pokemonList.add(pokemon3);
    return new ResponseEntity<>(pokemonList, HttpStatus.OK);
  }

}
