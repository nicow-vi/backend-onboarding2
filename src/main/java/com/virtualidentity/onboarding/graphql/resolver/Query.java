package com.virtualidentity.onboarding.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.model.Blog;
import com.virtualidentity.onboarding.graphql.repository.AuthorRepository;
import com.virtualidentity.onboarding.graphql.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {

  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private BlogRepository blogRepository;


  public Iterable<Author> getAllAuthors() {
    return authorRepository.findAll();
  }

  public Iterable<Blog> getAllBlogs() {
    return blogRepository.findAll();
  }

  public Author getAuthorById(Long id) {
    return authorRepository.findById(id).get();
  }

  public Blog getBlogById(Long id) {
    return blogRepository.findById(id).get();
  }

}
