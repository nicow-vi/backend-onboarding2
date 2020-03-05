package com.virtualidentity.onboarding.blogs.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.virtualidentity.onboarding.authors.rest.Author;
import com.virtualidentity.onboarding.authors.rest.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BlogRepositoryTest {

  @Autowired
  AuthorRepository authorRep;

  @Autowired
  BlogRepository blogRep;

  @Test
  public void WHEN_add_blog_THEN_blog_is_in_db() {
    // Arrange
    Author author1 = new Author("Nico", "Weingaertner");
    Blog blog1 = new Blog("Nice Title", "Good Text", author1);
    author1.addBlog(blog1);

    // Act
    authorRep.save(author1);
    blogRep.save(blog1);
    Blog blogFromDB = blogRep.findById(blog1.getId()).get();

    // Assert
    assertThat(blogRep.existsById(blog1.getId()), is(true));
    assertThat(blogFromDB.getTitle(), is("Nice Title"));
    assertThat(blogFromDB.getText(), is("Good Text"));
  }

  @Test
  public void WHEN_delete_blog_THEN_blog_is_deleted_not_author() {
    // Arrange
    Author author1 = new Author("Nico", "Wain");
    Blog blog1 = new Blog("Nice Title", "Good Text", author1);
    authorRep.save(author1);

    // Act
    author1.removeBlog(blog1);

    // Assert
    assertThat(blogRep.existsById(blog1.getId()), is(false));
    assertThat(authorRep.existsById(author1.getId()), is(true));
  }

  @Test
  public void WHEN_update_blog_THEN_blog_is_updated() {
    // Arrange
    Author author1 = new Author("Nico", "Wain");
    Blog blog1 = new Blog("Nice Title", "Good Text", author1);
    authorRep.save(author1);

    // Act
    Blog blogFromDb = blogRep.findById(blog1.getId()).get();
    blogFromDb.setTitle("Updated Title");
    blogFromDb.setText("Updated Text");
    blogRep.save(blogFromDb);

    // Assert
    assertThat(blogRep.existsById(blog1.getId()), is(true));
    assertThat(blogRep.findById(blog1.getId()).get().getTitle(), is("Updated Title"));
    assertThat(blogRep.findById(blog1.getId()).get().getText(), is("Updated Text"));
  }

}
