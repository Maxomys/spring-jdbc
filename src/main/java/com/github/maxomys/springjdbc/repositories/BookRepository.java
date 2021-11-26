package com.github.maxomys.springjdbc.repositories;

import com.github.maxomys.springjdbc.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {



}
