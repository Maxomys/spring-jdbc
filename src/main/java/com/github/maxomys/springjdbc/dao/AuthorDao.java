package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;

import java.util.List;

public interface AuthorDao {

    Author getById(Long id);

    Author findAuthorByName(String firstName, String lastName);

    List<Author> listAuthorByLastNameLike(String lastname);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);

}
