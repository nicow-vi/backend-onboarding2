package com.virtualidentity.onboarding.authors.rest;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorRepository extends PagingAndSortingRepository<AuthorEntity, Long> {

}
