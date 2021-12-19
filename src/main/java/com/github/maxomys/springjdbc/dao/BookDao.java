package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Book;

public interface BookDao {

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book findByISBN(String isbn);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);

}
