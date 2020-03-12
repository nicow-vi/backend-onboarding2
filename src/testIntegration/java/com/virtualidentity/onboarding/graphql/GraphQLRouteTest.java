package com.virtualidentity.onboarding.graphql;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.virtualidentity.onboarding.WebMvcTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class GraphQLRouteTest extends WebMvcTest {

  static final String URL = "/graphql";

  @Test
  public void WHEN_create_author_THEN_author_is_created() throws Exception {
    // Arrange
    String query = "mutation createAuthor($firstName: String!, $lastName: String!){" +
        " createAuthor(firstName: $firstName, lastName: $lastName) {" +
        "  firstName" +
        "  lastName" +
        "}}";

    JSONObject variables = new JSONObject()
        .put("firstName", "Niggo")
        .put("lastName", "Wein");

    // Act
    performPOST(URL, generateRequest(query, variables))

        // Assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.createAuthor.firstName").value("Niggo"))
        .andExpect(jsonPath("$.data.createAuthor.lastName").value("Wein"));
  }

  private String generateRequest(String query, JSONObject variables) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("query", query);

    if (variables != null) {
      jsonObject.put("variables", variables);
    }
    System.out.println(jsonObject.toString());
    return jsonObject.toString();
  }

}
