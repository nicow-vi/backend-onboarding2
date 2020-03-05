package com.virtualidentity.onboarding.authors.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.virtualidentity.onboarding.blogs.rest.Blog;
import com.virtualidentity.onboarding.blogs.rest.BlogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AuthorRepositoryTest {

  @Autowired
  AuthorRepository authorRep;

  @Autowired
  BlogRepository blogRep;

  @Test
  public void WHEN_add_author_THEN_author_is_in_db_with_correct_values() {
    // Arrange
    Author author1 = new Author("Nico", "Weingaertner");

    // Act
    authorRep.save(author1);
    Author authorFromDb = authorRep.findById(author1.getId()).get();

    // Assert
    assertThat(authorRep.existsById(author1.getId()), is(true));
    assertThat(authorFromDb.getFirstName(), is("Nico"));
    assertThat(authorFromDb.getLastName(), is("Weingaertner"));
  }

  @Test
  public void WHEN_add_author_and_blogs_THEN_author_and_blogs_is_in_db() {
    // Arrange
    Author author1 = new Author("Nico", "Wain");
    Blog blog1 = new Blog("Nice Title", "Good Text", author1);
    Blog blog2 = new Blog("Moby Dick", "Whale", author1);

    // Act
    authorRep.save(author1);

    // Assert
    assertThat(authorRep.existsById(author1.getId()), is(true));
    assertThat(blogRep.existsById(blog1.getId()), is(true));
    assertThat(blogRep.existsById(blog2.getId()), is(true));
  }

  @Test
  public void WHEN_delete_author_THEN_author_and_blogs_deleted() {
    // Arrange
    Author author1 = new Author("Nico", "Weingaertner");
    Blog blog1 = new Blog("Nice Title", "Good Text", author1);
    Blog blog2 = new Blog("Moby Dick", "Whale", author1);
    authorRep.save(author1);

    // Act
    authorRep.deleteById(author1.getId());

    // Assert
    assertThat(authorRep.existsById(author1.getId()), is(false));
    assertThat(blogRep.existsById(blog1.getId()), is(false));
    assertThat(blogRep.existsById(blog2.getId()), is(false));
  }

  @Test
  public void WHEN_updating_author_THEN_author_is_updated() {
    // Arrange
    Author author1 = new Author("Nico", "Weingaertner");
    authorRep.save(author1);

    // Act
    author1.setFirstName("Updated Firstname");
    author1.setLastName("Updated Lastname");
    authorRep.save(author1);
    Author authorFromDb = authorRep.findById(author1.getId()).get();

    //Assert
    assertThat(authorFromDb.getFirstName(), is("Updated Firstname"));
    assertThat(authorFromDb.getLastName(), is("Updated Lastname"));
  }
}
