package com.virtualidentity.onboarding.blogs.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.virtualidentity.onboarding.ApiMatchers;
import com.virtualidentity.onboarding.WebMvcTest;
import com.virtualidentity.onboarding.authors.rest.AuthorEntity;
import com.virtualidentity.onboarding.authors.rest.AuthorRepository;
import com.virtualidentity.onboarding.generated.model.Blog;
import com.virtualidentity.onboarding.generated.model.BlogList;
import com.virtualidentity.onboarding.generated.model.InlineResponse2001;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BlogControllerTest extends WebMvcTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  AuthorRepository authorRepository;
  @Autowired
  BlogRepository blogRepository;
  private static final String BLOG_URL = "/blogs/";


  @BeforeEach
  public void clear_database() {
    authorRepository.deleteAll();
    blogRepository.deleteAll();
  }

  @Test
  public void WHEN_get_all_blogs_THEN_get_all_blogs() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Niggo", "Wein");
    AuthorEntity author2 = new AuthorEntity("Lili", "Mihova");
    BlogEntity blog1 = new BlogEntity("Titel", "Text", author1);
    BlogEntity blog2 = new BlogEntity("Titel2", "Text2", author2);
    blogRepository.save(blog1);
    blogRepository.save(blog2);
    // Act
    performGET(BLOG_URL)
        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(BlogList.class));
  }

  @Test
  public void WHEN_get_all_blogs_with_title_query_THEN_get_blogs_with_matching_title()
      throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Niggo", "Wein");
    AuthorEntity author2 = new AuthorEntity("Lili", "Mihova");
    BlogEntity blog1 = new BlogEntity("Harry Potter", "In der Kammer des Schreckens", author1);
    BlogEntity blog2 = new BlogEntity("Herr der Ringe", "Mein Schatz!", author2);
    blogRepository.save(blog1);
    blogRepository.save(blog2);
    // Act
    performGET(BLOG_URL + "?title=ringe")
        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(BlogList.class))
        .andExpect(jsonPath("$..title").value("Herr der Ringe"));

  }

  @Test
  public void WHEN_get_all_blogs_with_text_query_THEN_get_blogs_with_matching_text()
      throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Niggo", "Wein");
    AuthorEntity author2 = new AuthorEntity("Lili", "Mihova");
    BlogEntity blog1 = new BlogEntity("Harry Potter", "In der Kammer des Schreckens", author1);
    BlogEntity blog2 = new BlogEntity("Herr der Ringe", "Mein Schatz!", author2);
    blogRepository.save(blog1);
    blogRepository.save(blog2);
    // Act
    performGET(BLOG_URL + "?text=Schatz!")
        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(BlogList.class))
        .andExpect(jsonPath("$..text").value("Mein Schatz!"));

  }

  @Test
  public void WHEN_get_blog_by_id_THEN_get_blog_by_id() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Get", "Blogs");
    BlogEntity blog1 = new BlogEntity("Titel", "Text", author1);
    blogRepository.save(blog1);
    String blogId = blog1.getId().toString();
    // Act
    performGET(BLOG_URL + blogId)
        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(Blog.class));
  }

  @Test
  public void WHEN_delete_blog_THEN_blog_is_deleted() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Delete", "Blogs");
    BlogEntity blog1 = new BlogEntity("Titel", "Text", author1);
    blogRepository.save(blog1);
    assertThat(blogRepository.findById(blog1.getId()).isPresent(), is(true));
    String blogId = blog1.getId().toString();
    // Act
    performDELETE(BLOG_URL + blogId)
        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(InlineResponse2001.class));
    assertThat(blogRepository.findById(blog1.getId()).isPresent(), is(false));

  }

  @Test
  public void WHEN_update_blog_THEN_blog_is_updated() throws Exception {
    // Arrange
    String RequestBody = "{\n"
        + "  \"title\": \"Freshly\",\n"
        + "  \"text\": \"Updated\"\n"
        + "}";
    AuthorEntity author1 = new AuthorEntity("Old", "User");
    BlogEntity blog1 = new BlogEntity("Old", "Blog", author1);
    blogRepository.save(blog1);
    String id = blog1.getId().toString();

    // Act
    performPUT(BLOG_URL + id, RequestBody)

        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(Blog.class))
        .andExpect(jsonPath("$.title").value("Freshly"))
        .andExpect(jsonPath("$.text").value("Updated"));

  }
}
