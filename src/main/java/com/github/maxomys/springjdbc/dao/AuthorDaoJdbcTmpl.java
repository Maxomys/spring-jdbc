package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("authorDaoJdbcTmpl")
public class AuthorDaoJdbcTmpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoJdbcTmpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        String sql = "SELECT author.id AS id, first_name, last_name, book.id AS book_id, book.title, book.isbn, book.publisher\n" +
                    "\tFROM author LEFT JOIN book ON author.id = book.author_id WHERE author.id = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);

        //return jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", getRowMapper(), id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name = ? AND last_name = ?",
                                                getRowMapper(), firstName, lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                                author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                                author.getFirstName(), author.getLastName(), author.getId());

        return getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }

}
