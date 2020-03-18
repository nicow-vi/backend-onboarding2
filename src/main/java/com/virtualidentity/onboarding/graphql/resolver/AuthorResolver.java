package com.virtualidentity.onboarding.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.model.Blog;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AuthorResolver implements GraphQLResolver<Author> {

  public List<Blog> getBlog(Author author) {
    return author.getBlogs();
  }
}
