package com.virtualidentity.onboarding.graphql.repository;

import com.virtualidentity.onboarding.graphql.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
