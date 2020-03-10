package com.virtualidentity.onboarding.authors.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.virtualidentity.onboarding.blogs.rest.BlogController;
import com.virtualidentity.onboarding.blogs.rest.BlogEntity;
import com.virtualidentity.onboarding.blogs.rest.BlogRepository;
import com.virtualidentity.onboarding.common.rest.controller.BaseController;
import com.virtualidentity.onboarding.generated.AuthorsApi;
import com.virtualidentity.onboarding.generated.model.Author;
import com.virtualidentity.onboarding.generated.model.AuthorLinks;
import com.virtualidentity.onboarding.generated.model.AuthorList;
import com.virtualidentity.onboarding.generated.model.AuthorListLinks;
import com.virtualidentity.onboarding.generated.model.AuthorRequest;
import com.virtualidentity.onboarding.generated.model.Blog;
import com.virtualidentity.onboarding.generated.model.BlogList;
import com.virtualidentity.onboarding.generated.model.BlogRequest;
import com.virtualidentity.onboarding.generated.model.HalLink;
import com.virtualidentity.onboarding.generated.model.InlineResponse200;
import java.math.BigDecimal;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController extends BaseController implements AuthorsApi {

  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  BlogRepository blogRepository;

  @Override
  public ResponseEntity<Author> createAuthor(@Valid AuthorRequest authorRequestBody)
      throws Exception {

    AuthorEntity authorEntity = new AuthorEntity(authorRequestBody.getFirstname(),
        authorRequestBody.getLastname());
    AuthorEntity savedAuthor = authorRepository.save(authorEntity);
    Author author = entityToAuthor(savedAuthor);

    return new ResponseEntity<>(author, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Author> getAuthorById(@PathVariable("id") BigDecimal id) {
    Optional<AuthorEntity> searchedAuthor = authorRepository.findById(id.longValue());
    Author author = new Author();
    if (searchedAuthor.isPresent()) {
      AuthorEntity authorFromDB = searchedAuthor.get();
      author = entityToAuthor(authorFromDB);
      return new ResponseEntity<>(author, HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Author not found!");
    }
  }

  @Override
  public ResponseEntity<AuthorList> getAuthors(
      @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
      @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset)
      throws Exception {

    AuthorList authorList = new AuthorList();
    authorRepository.findAll(PageRequest.of(offset, limit))
        .forEach(authorEntity -> authorList.addEmbeddedItem(entityToAuthor(authorEntity)));
    AuthorListLinks authorListLinks = new AuthorListLinks()
        .self(getHalGetLink(methodOn(this.getClass()).getAuthors(limit, offset)))
        .next(getHalGetLink(methodOn(this.getClass()).getAuthors(limit, offset + 1)));
    if (offset != 0) {
      authorListLinks
          .previous(getHalGetLink(methodOn(this.getClass()).getAuthors(limit, offset - 1)));
    }
    authorList.links(authorListLinks);

    return new ResponseEntity<>(authorList, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<InlineResponse200> deleteAuthorById(BigDecimal id) throws Exception {

    Optional<AuthorEntity> searchedAuthor = authorRepository.findById(id.longValue());
    if (searchedAuthor.isPresent()) {
      authorRepository.deleteById(id.longValue());
      InlineResponse200 response200 = new InlineResponse200()
          .authors(getHalGetLink(methodOn(this.getClass()).getAuthors(10, 0)));

      return new ResponseEntity<>(response200, HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Could not find author");
    }
  }

  @Override
  public ResponseEntity<Author> updateAuthorById(BigDecimal id,
      @Valid AuthorRequest authorRequestBody)
      throws Exception {
    Optional<AuthorEntity> searchedAuthor = authorRepository.findById(id.longValue());
    if (searchedAuthor.isPresent()) {
      AuthorEntity authorFromDB = searchedAuthor.get();
      authorFromDB.setFirstname(authorRequestBody.getFirstname());
      authorFromDB.setLastname(authorRequestBody.getLastname());
      AuthorEntity updatedAuthor = authorRepository.save(authorFromDB);
      Author author = entityToAuthor(updatedAuthor);

      return new ResponseEntity<>(author, HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Could not find author");
    }
  }

  private Author entityToAuthor(AuthorEntity authorEntity) {
    return new Author()
        .id(BigDecimal.valueOf(authorEntity.getId()))
        .firstname(authorEntity.getFirstname())
        .lastname(authorEntity.getLastname())
        .links(new AuthorLinks().self(getAuthorLink(authorEntity.getId())));
  }

  private HalLink getAuthorLink(Long id) {
    return getHalGetLink(methodOn(this.getClass()).getAuthorById(BigDecimal.valueOf(id)));
  }

  @Override
  public ResponseEntity<Blog> createBlog(BigDecimal id, @Valid BlogRequest blogRequest)
      throws Exception {
    Optional<AuthorEntity> searchedAuthor = authorRepository.findById(id.longValue());
    BlogController blogController = new BlogController();

    if (searchedAuthor.isPresent()) {
      AuthorEntity authorEntity = searchedAuthor.get();
      BlogEntity blogEntity = new BlogEntity(blogRequest.getTitle(), blogRequest.getText(),
          authorEntity);
      blogRepository.save(blogEntity);
      Optional<BlogEntity> searchedBlog = blogRepository.findById(blogEntity.getId());
      if (searchedBlog.isPresent()) {
        BlogEntity blogFromDB = searchedBlog.get();
        Blog blog = blogController.entityToBlog(blogFromDB);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);
      } else {
        throw new IllegalArgumentException("Could not find blog");
      }

    } else {
      throw new IllegalArgumentException("Could not find author");
    }

  }

  @Override
  public ResponseEntity<BlogList> getBlogsByAuthor(BigDecimal id, @Valid Integer limit,
      @Valid Integer offset) throws Exception {
    BlogList blogList = new BlogList();
    BlogController blogController = new BlogController();
    AuthorEntity authorFromDb = authorRepository.findById(id.longValue()).get();
    authorFromDb.getBlogs()
        .forEach(blogEntity -> {
          try {
            blogList.addEmbeddedItem(blogController.entityToBlog(blogEntity));
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
    blogList.links(new AuthorListLinks()
        .self(getHalGetLink(methodOn(this.getClass()).getBlogsByAuthor(id, limit, offset))));
    return new ResponseEntity<>(blogList, HttpStatus.OK);
  }
}

