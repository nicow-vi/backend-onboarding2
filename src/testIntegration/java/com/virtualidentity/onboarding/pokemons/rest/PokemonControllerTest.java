package com.virtualidentity.onboarding.pokemons.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.virtualidentity.onboarding.WebMvcTest;
import org.junit.jupiter.api.Test;

public class PokemonControllerTest extends WebMvcTest {
  private static final String URL = "/pokemons/";

  @Test
  public void WHEN_get_pokemons_THEN_get_names() throws Exception {
    // Act
    performGET(URL)
        // Assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].name").isArray());
  }
}
