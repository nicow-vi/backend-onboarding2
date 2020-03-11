package com.virtualidentity.onboarding.blogs.rest;

import com.virtualidentity.onboarding.authors.rest.AuthorEntity;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BlogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String text;

  @ManyToOne(cascade = CascadeType.ALL)
  private AuthorEntity author;

  public BlogEntity() {
    //needed for JPA
  }

  public BlogEntity(String title, String text, AuthorEntity author) {
    this.title = title;
    this.text = text;
    this.author = author;
    author.addBlog(this);
  }

  @Override
  public String toString() {
    return String.format(
        "Blog[id=%d, title='%s', text='%s']",
        id, title, text);
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setAuthor(AuthorEntity author) {
    this.author = author;
  }

  public Long getAuthorId() {
    return this.author.getId();
  }
}