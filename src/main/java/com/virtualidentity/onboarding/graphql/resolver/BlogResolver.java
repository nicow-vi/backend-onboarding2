package com.virtualidentity.onboarding.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.model.Blog;
import com.virtualidentity.onboarding.graphql.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogResolver implements GraphQLResolver<Blog> {

  @Autowired
  AuthorRepository authorRepository;

  public Author getAuthor(Blog blog) {
    Author author = authorRepository.findById(blog.getAuthorId()).get();
    return author;
  }
}
