package com.virtualidentity.onboarding.authors.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.virtualidentity.onboarding.ApiMatchers;
import com.virtualidentity.onboarding.WebMvcTest;
import com.virtualidentity.onboarding.blogs.rest.BlogRepository;
import com.virtualidentity.onboarding.generated.model.Author;
import com.virtualidentity.onboarding.generated.model.AuthorList;
import com.virtualidentity.onboarding.generated.model.Blog;
import com.virtualidentity.onboarding.generated.model.Error;
import com.virtualidentity.onboarding.generated.model.InlineResponse200;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorControllerTest extends WebMvcTest {

  @Autowired
  AuthorRepository authorRepository;
  @Autowired
  BlogRepository blogRepository;
  private static final String BLOG_URL = "/authors/1/blogs/";

  private static final String URL = "/authors/";

  @Test
  public void WHEN_create_author_THEN_author_is_created() throws Exception {
    // Arrange
    String RequestBody = new String("{\n"
        + "  \"firstname\": \"Test\",\n"
        + "  \"lastname\": \"User\"\n"
        + "}");

    // Act
    performPOST(URL, RequestBody)

        // Assert
        .andExpect(status().isCreated())
        .andExpect(ApiMatchers.responseMatchesModel(Author.class))
        .andExpect(jsonPath("$.firstname").value("Test"))
        .andExpect(jsonPath("$.lastname").value("User"));

  }

  @Test
  public void WHEN_get_author_by_id_THEN_get_correct_author() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Get", "User");
    authorRepository.save(author1);
    String id = author1.getId().toString();

    // Act
    performGET(URL + id)

        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(Author.class))
        .andExpect(jsonPath("$.firstname").value("Get"))
        .andExpect(jsonPath("$.lastname").value("User"));
  }


  @Test
  public void GIVEN_not_existing_id_THEN_get_author_by_id_THEN_response_is_error()
      throws Exception {
    // Arrange
    String id = "999";

    // Act
    performGET(URL + id)

        // Assert
        .andExpect(status().isInternalServerError())
        .andExpect(ApiMatchers.responseMatchesModel(Error.class));
  }

  @Test
  public void WHEN_get_all_authors_THEN_response_is_list_of_authors() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Test", "User");
    authorRepository.save(author1);

    // Act
    performGET(URL)

        // Arrange
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(AuthorList.class));

  }

  @Test
  public void WHEN_delete_author_THEN_author_is_deleted() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Delete", "Me");
    authorRepository.save(author1);
    String id = author1.getId().toString();

    // Act
    performDELETE(URL + id)

        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(InlineResponse200.class));
  }

  @Test
  public void WHEN_update_author_THEN_author_has_new_values() throws Exception {
    // Arrange
    String RequestBody = "{\n"
        + "  \"firstname\": \"Freshly\",\n"
        + "  \"lastname\": \"Updated\"\n"
        + "}";
    AuthorEntity author1 = new AuthorEntity("Old", "User");
    authorRepository.save(author1);
    String id = author1.getId().toString();

    // Act
    performPUT(URL + id, RequestBody)

        // Assert
        .andExpect(status().isOk())
        .andExpect(ApiMatchers.responseMatchesModel(Author.class))
        .andExpect(jsonPath("$.firstname").value("Freshly"))
        .andExpect(jsonPath("$.lastname").value("Updated"));

  }

  @Test
  public void WHEN_create_blog_THEN_blog_is_created() throws Exception {
    // Arrange
    AuthorEntity author1 = new AuthorEntity("Get", "User");
    authorRepository.save(author1);
    String RequestBody = new String("{\n"
        + "  \"title\": \"Test\",\n"
        + "  \"text\": \"Text\"\n"
        + "}");

    // Act
    performPOST(BLOG_URL, RequestBody)

        // Assert
        .andExpect(status().isCreated())
        .andExpect(ApiMatchers.responseMatchesModel(Blog.class))
        .andExpect(jsonPath("$.title").value("Test"))
        .andExpect(jsonPath("$.text").value("Text"));
  }

}
