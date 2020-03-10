package com.virtualidentity.onboarding.authors.rest;

import com.virtualidentity.onboarding.blogs.rest.BlogEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Indexed
@Entity
public class AuthorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Field
  private String firstName;
  @Field
  private String lastName;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BlogEntity> blogs;

  public AuthorEntity() {
    //needed for JPA
  }

  public AuthorEntity(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.blogs = new ArrayList<>();
  }

  @Override
  public String toString() {
    return String.format(
        "Customer[id=%d, firstName='%s', lastName='%s']",
        id, firstName, lastName);
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void addBlog(BlogEntity blog) {
    blog.setAuthor(this);
    blogs.add(blog);
  }

  public void setBlogs(List<BlogEntity> blogs) {
    this.blogs = blogs;
  }

  public List<BlogEntity> getBlogs() {
    return blogs;
  }

  public void removeBlog(BlogEntity blog) {
    blog.setAuthor(null);
    blogs.remove(blog);
  }
}

