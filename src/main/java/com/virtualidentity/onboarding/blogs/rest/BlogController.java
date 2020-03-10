package com.virtualidentity.onboarding.blogs.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.virtualidentity.onboarding.authors.rest.AuthorRepository;
import com.virtualidentity.onboarding.common.rest.controller.BaseController;
import com.virtualidentity.onboarding.generated.BlogsApi;
import com.virtualidentity.onboarding.generated.model.AuthorLinks;
import com.virtualidentity.onboarding.generated.model.AuthorListLinks;
import com.virtualidentity.onboarding.generated.model.Blog;
import com.virtualidentity.onboarding.generated.model.BlogList;
import com.virtualidentity.onboarding.generated.model.BlogRequest;
import com.virtualidentity.onboarding.generated.model.HalLink;
import com.virtualidentity.onboarding.generated.model.InlineResponse2001;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController extends BaseController implements BlogsApi {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  AuthorRepository authorRepository;

  @Autowired
  BlogRepository blogRepository;


  @Override
  public ResponseEntity<Blog> getBlogById(BigDecimal id) throws Exception {
    Optional<BlogEntity> searchedBlog = blogRepository.findById(id.longValue());
    if (searchedBlog.isPresent()) {
      BlogEntity blogFromDB = searchedBlog.get();
      Blog blog = entityToBlog(blogFromDB);
      return new ResponseEntity<>(blog, HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Could not find blog");
    }
  }


  @Override
  public ResponseEntity<BlogList> getAllBlogs(@PathVariable String title, @PathVariable String text,
      @PathVariable String authorfirstname, @PathVariable String authorlastname,
      @PathVariable Integer limit, @PathVariable Integer offset) throws Exception {

    List<BlogEntity> results = new ArrayList<>();
    BlogList blogList = new BlogList();

    if (!Optional.ofNullable(title).isPresent() && !Optional.ofNullable(text).isPresent()
        && !Optional.ofNullable(authorfirstname).isPresent() && !Optional.ofNullable(authorlastname)
        .isPresent()) {
      blogRepository.findAll()
          .forEach(blogEntity -> {
            try {
              blogList.addEmbeddedItem(entityToBlog(blogEntity));
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
      blogList.links(new AuthorListLinks()
          .self(getHalGetLink(
              methodOn(this.getClass()).getAllBlogs(null, null, null, null, limit, offset))));
      return new ResponseEntity<>(blogList, HttpStatus.OK);
    }

    FullTextEntityManager fullTextEntityManager
        = Search.getFullTextEntityManager(entityManager);
    fullTextEntityManager.createIndexer().startAndWait();

    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(BlogEntity.class)
        .get();

    final BooleanJunction<BooleanJunction> combinedQuery = queryBuilder
        .bool();

    if (Optional.ofNullable(title).isPresent()) {
      combinedQuery
          .should(queryBuilder
              .keyword()
              .fuzzy()
              .withEditDistanceUpTo(2)
              .onField("title")
              .matching(title)
              .createQuery());
    }
    if (Optional.ofNullable(text).isPresent()) {
      combinedQuery
          .should(queryBuilder
              .keyword()
              .fuzzy()
              .withEditDistanceUpTo(2)
              .onField("text")
              .matching(text)
              .createQuery());
    }
    if (Optional.ofNullable(authorfirstname).isPresent()) {
      combinedQuery
          .should(queryBuilder
              .keyword()
              .fuzzy()
              .withEditDistanceUpTo(2)
              .onField("author.firstname")
              .matching(authorfirstname)
              .createQuery());
    }
    if (Optional.ofNullable(authorlastname).isPresent()) {
      combinedQuery
          .should(queryBuilder
              .keyword()
              .fuzzy()
              .withEditDistanceUpTo(2)
              .onField("author.lastname")
              .matching(authorlastname)
              .createQuery());
    }

    org.hibernate.search.jpa.FullTextQuery jpaQuery
        = fullTextEntityManager.createFullTextQuery(combinedQuery.createQuery(), BlogEntity.class);

    results.addAll(jpaQuery.getResultList());

    results
        .forEach(blogEntity -> {
          try {
            blogList.addEmbeddedItem(entityToBlog(blogEntity));
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
    blogList.links(new AuthorListLinks()
        .self(getHalGetLink(methodOn(this.getClass())
            .getAllBlogs(title, text, authorfirstname, authorlastname, limit, offset))));
    return new ResponseEntity<>(blogList, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<InlineResponse2001> deleteBlogById(BigDecimal id) throws Exception {
    Optional<BlogEntity> searchedBlog = blogRepository.findById(id.longValue());
    if (searchedBlog.isPresent()) {
      blogRepository.deleteById(id.longValue());
      InlineResponse2001 response2001 = new InlineResponse2001()
          .blogs(
              getHalGetLink(methodOn(this.getClass()).getAllBlogs(null, null, null, null, 0, 0)));

      return new ResponseEntity<>(response2001, HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Could not find author");
    }
  }

  @Override
  public ResponseEntity<Blog> updateBlogById(BigDecimal id, @Valid BlogRequest blogRequest)
      throws Exception {
    Optional<BlogEntity> searchedBlog = blogRepository.findById(id.longValue());
    if (searchedBlog.isPresent()) {
      BlogEntity blogFromDb = searchedBlog.get();
      blogFromDb.setText(blogRequest.getText());
      blogFromDb.setTitle(blogRequest.getTitle());
      BlogEntity updatedBLog = blogRepository.save(blogFromDb);
      return new ResponseEntity<>(entityToBlog(updatedBLog), HttpStatus.OK);
    } else {
      throw new IllegalArgumentException("Could not find blog");
    }
  }

  public Blog entityToBlog(BlogEntity blogEntity) throws Exception {
    return new Blog()
        .id(BigDecimal.valueOf(blogEntity.getId()))
        .title(blogEntity.getTitle())
        .text(blogEntity.getText())
        .links(new AuthorLinks().self(getBlogLink(blogEntity.getId())));
  }

  private HalLink getBlogLink(Long id) throws Exception {
    return getHalGetLink(methodOn(this.getClass())
        .getBlogById(BigDecimal.valueOf(id)));
  }

}
