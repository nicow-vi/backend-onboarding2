package com.virtualidentity.onboarding.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.virtualidentity.onboarding.graphql.model.Author;
import com.virtualidentity.onboarding.graphql.model.Blog;
import com.virtualidentity.onboarding.graphql.repository.AuthorRepository;
import com.virtualidentity.onboarding.graphql.repository.BlogRepository;
import java.util.Optional;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private BlogRepository blogRepository;

  public Author createAuthor(String firstName, String lastName) {
    Author author = new Author(firstName, lastName);
    return authorRepository.save(author);
  }

  public Author updateAuthor(Long id, String firstName, String lastName) throws NotFoundException {
    Optional<Author> searchedAuthor = authorRepository.findById(id);
    if (searchedAuthor.isPresent()) {
      Author author = searchedAuthor.get();
      if (firstName != null) {
        author.setFirstName(firstName);
      }
      if (lastName != null) {
        author.setLastName(lastName);
      }
      return authorRepository.save(author);
    } else {
      throw new NotFoundException("No author with this id found!");
    }
  }

  public Boolean deleteAuthor(Long id) {
    authorRepository.deleteById(id);
    return true;
  }


  public Blog createBlog(String title, String text, Long id) {
    Author author = authorRepository.findById(id).get();
    Blog blog = new Blog(title, text, author);
    return blogRepository.save(blog);
  }

  public Blog updateBlog(Long id, String title, String text) throws NotFoundException {
    Optional<Blog> searchedBlog = blogRepository.findById(id);
    if (searchedBlog.isPresent()) {
      Blog blog = searchedBlog.get();
      if (title != null) {
        blog.setTitle(title);
      }
      if (text != null) {
        blog.setText(text);
      }
      return blogRepository.save(blog);
    } else {
      throw new NotFoundException("No blog with this id found!");
    }
  }

  public Boolean deleteBlog(Long id) {
    blogRepository.deleteById(id);
    return true;
  }

}
