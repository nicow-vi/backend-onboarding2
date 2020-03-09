package com.virtualidentity.onboarding.blogs.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.virtualidentity.onboarding.authors.rest.AuthorEntity;
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
    AuthorEntity author1 = new AuthorEntity("Nico", "Weingaertner");
    BlogEntity blog1 = new BlogEntity("Nice Title", "Good Text", author1);

    // Act
    authorRep.save(author1);
    BlogEntity blogFromDB = blogRep.findById(blog1.getId()).get();

    // Assert
    assertThat(blogRep.existsById(blog1.getId()), is(true));
    assertThat(blogFromDB.getTitle(), is("Nice Title"));
    assertThat(blogFromDB.getText(), is("Good Text"));
  }

  @Test
  public void WHEN_delete_blog_THEN_blog_is_deleted_not_author() {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Nico", "Wain");
    BlogEntity blog1 = new BlogEntity("Nice Title", "Good Text", author1);
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
    AuthorEntity author1 = new AuthorEntity("Nico", "Wain");
    BlogEntity blog1 = new BlogEntity("Nice Title", "Good Text", author1);
    authorRep.save(author1);

    // Act
    BlogEntity blogFromDb = blogRep.findById(blog1.getId()).get();
    blogFromDb.setTitle("Updated Title");
    blogFromDb.setText("Updated Text");
    blogRep.save(blogFromDb);

    // Assert
    assertThat(blogRep.existsById(blog1.getId()), is(true));
    assertThat(blogRep.findById(blog1.getId()).get().getTitle(), is("Updated Title"));
    assertThat(blogRep.findById(blog1.getId()).get().getText(), is("Updated Text"));
  }

}
