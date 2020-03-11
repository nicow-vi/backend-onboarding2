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
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Indexed
@Entity
public class AuthorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Field
  private String firstname;
  @Field
  private String lastname;

  @ContainedIn
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BlogEntity> blogs;

  public AuthorEntity() {
    //needed for JPA
  }

  public AuthorEntity(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.blogs = new ArrayList<>();
  }

  @Override
  public String toString() {
    return String.format(
        "Customer[id=%d, firstName='%s', lastName='%s']",
        id, firstname, lastname);
  }

  public Long getId() {
    return id;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstName) {
    this.firstname = firstName;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastName) {
    this.lastname = lastName;
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

