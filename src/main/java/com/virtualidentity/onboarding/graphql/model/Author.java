package com.virtualidentity.onboarding.graphql.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String firstName;
  private String lastName;

  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Blog> blogs;

  public Author() {
    //needed for JPA
  }

  public Author(String firstName, String lastName) {
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

  public void setId(Long id) {
    this.id = id;
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

  public void addBlog(Blog blog) {
    blog.setAuthor(this);
    blogs.add(blog);
  }

  public void setBlogs(List<Blog> blogs) {
    this.blogs = blogs;
  }

  public List<Blog> getBlogs() {
    return blogs;
  }

  public void removeBlog(Blog blog) {
    blog.setAuthor(null);
    blogs.remove(blog);
  }
}

