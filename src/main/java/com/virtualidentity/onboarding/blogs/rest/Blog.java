package com.virtualidentity.onboarding.blogs.rest;

import com.virtualidentity.onboarding.authors.rest.Author;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Blog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String text;

  @ManyToOne
  private Author author;

  public Blog() {
    //needed for JPA
  }

  public Blog(String title, String text, Author author) {
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

  public void setAuthor(Author author) {
    this.author = author;
  }
}
