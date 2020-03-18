package com.virtualidentity.onboarding.graphql.repository;

import com.virtualidentity.onboarding.graphql.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

}
