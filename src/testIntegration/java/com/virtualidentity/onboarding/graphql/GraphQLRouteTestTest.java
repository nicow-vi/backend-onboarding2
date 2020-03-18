package com.virtualidentity.onboarding.graphql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.repository.AuthorRepository;
import com.virtualidentity.onboarding.graphql.repository.BlogRepository;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@GraphQLTest
public class GraphQLRouteTestTest {

  @Autowired
  private GraphQLTestTemplate graphQLTestTemplate;

  @MockBean
  public AuthorRepository authorRepository;

  @MockBean
  public BlogRepository blogRepository;

  @Test
  public void WHEN_create_author_THEN_author_is_created() throws Exception {
    // Arrange
    Author author = new Author("Niggo", "Wein");
    author.setId(1L);
    when(authorRepository.save(any())).thenReturn(author);

    ObjectNode variables = new ObjectMapper().createObjectNode();
    variables.put("firstName", "Niggo");
    variables.put("lastName", "Wein");

    // Act
    GraphQLResponse response = graphQLTestTemplate
        .perform("create-author.graphql", variables);

    // Assert
    System.out.println(response);
    assertTrue(response.isOk());
    assertEquals("1", response.get("$.data.createAuthor.id"));
    assertEquals("Niggo", response.get("$.data.createAuthor.firstName"));
  }

  @Test
  public void WHEN_get_author_by_id_Then_get_author() throws IOException {
    // Arrange
    Author author = new Author("Niggo", "Wein");
    author.setId(1L);
    when(authorRepository.findById(any())).thenReturn(java.util.Optional.of(author));

    // Act
    GraphQLResponse response =
        graphQLTestTemplate.postForResource("get-author-by-id.graphql");

    // Assert
    assertTrue(response.isOk());
    assertEquals("1", response.get("$.data.getAuthorById.id"));
    assertEquals("Niggo", response.get("$.data.getAuthorById.firstName"));
  }

}
