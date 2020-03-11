package com.virtualidentity.onboarding.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.model.Blog;
import com.virtualidentity.onboarding.graphql.repository.BlogRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorResolver implements GraphQLResolver<Author> {

  @Autowired
  BlogRepository blogRepository;

  public List<Blog> getBlog(Author author) {
    return author.getBlogs();
  }
}
