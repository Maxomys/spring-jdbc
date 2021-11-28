package com.github.maxomys.springjdbc;

import com.github.maxomys.springjdbc.dao.AuthorDao;
import com.github.maxomys.springjdbc.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles({"local"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"com.github.maxomys.springjdbc.dao"})
public class AuthorDaoJdbcTmplIT {

    @Autowired
    @Qualifier("jdbcTmpl")
    AuthorDao authorDao;

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthorByName() {
        String firstName = "Eric";
        String lastName = "Evans";

        Author author = authorDao.findAuthorByName(firstName, lastName);

        assertNotNull(author);
        assertEquals(firstName, author.getFirstName());
        assertEquals(lastName, author.getLastName());
    }

    @Test
    void testSaveNewAuthor() {
        Author newAuthor = new Author("Peter", "Hamilton");
        Author savedAuthor = authorDao.saveNewAuthor(newAuthor);

        assertNotNull(savedAuthor);
        assertEquals("Hamilton", savedAuthor.getLastName());
    }

    @Test
    void testUpdateAuthor() {
        Author newAuthor = new Author("John", "Original");

        Author savedAuthor = authorDao.saveNewAuthor(newAuthor);
        savedAuthor.setLastName("Updated");

        Author updatedAuthor = authorDao.updateAuthor(savedAuthor);

        assertEquals("Updated", updatedAuthor.getLastName());
    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author("Jane", "Doe");

        Author savedAuthor = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(savedAuthor.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> authorDao.getById(savedAuthor.getId()));
    }

}
