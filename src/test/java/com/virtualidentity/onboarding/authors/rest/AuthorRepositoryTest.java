package com.virtualidentity.onboarding.authors.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AuthorRepositoryTest {

  @Autowired
  AuthorRepository rep;

  @Test
  public void WHEN_add_author_THEN_author_is_in_db() {
    Author author1 = new Author("Nico", "Wain");
    rep.save(author1);
    boolean isAuthorInDb = rep.existsById(author1.getId());
    assertThat(isAuthorInDb, is(true));
  }
}
