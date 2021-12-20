package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Book;

import java.util.List;

public interface BookDao {

    List<Book> findAllBooks();

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book findByISBN(String isbn);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);

}
