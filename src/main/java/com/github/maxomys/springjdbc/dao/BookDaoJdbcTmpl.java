package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("bookDaoJdbcTmpl")
public class BookDaoJdbcTmpl implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    public BookDaoJdbcTmpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE title = ?", getRowMapper(), title);
    }

    @Override
    public Book saveNewBook(Book book) {
        if (book.getAuthor() != null) {
            jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                                    book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor().getId());
        } else {
            jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), null);
        }

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return getById(createdId);
    }

    @Override
    public Book updateBook(Book book) {
        if (book.getAuthor() != null) {
            jdbcTemplate.update("UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE book.id = ?",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor().getId());
        } else {
            jdbcTemplate.update("UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE book.id = ?",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), null);
        }

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return getById(createdId);
    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", id);
    }

    private RowMapper<Book> getRowMapper() {
        return new BookMapper();
    }

}
