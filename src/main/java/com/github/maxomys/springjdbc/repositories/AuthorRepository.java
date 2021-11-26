package com.github.maxomys.springjdbc.repositories;

import com.github.maxomys.springjdbc.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {



}
