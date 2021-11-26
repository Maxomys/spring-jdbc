package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author findAuthorByName(String firstName, String lastName);

    Author saveNewAuthor(Author author);

}
