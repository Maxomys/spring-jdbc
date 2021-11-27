package com.github.maxomys.springjdbc;

import com.github.maxomys.springjdbc.dao.BookDao;
import com.github.maxomys.springjdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"com.github.maxomys.springjdbc.dao"})
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetBook() {
        Book book = bookDao.getById(1L);

        assertNotNull(book);
    }

    @Test
    void testGetBookByTitle() {
        String title = "Spring in Action, 5th Edition";

        Book book = bookDao.findBookByTitle(title);

        assertNotNull(book);
        assertEquals(title, book.getTitle());
    }

    @Test
    void testSaveNewBook() {
        String title = "My Book 3";

        Book newBook = new Book(title, null, null);
        newBook.setAuthorId(1L);

        Book savedBook = bookDao.saveNewBook(newBook);

        assertNotNull(savedBook);
        assertEquals(title, savedBook.getTitle());
    }

    @Test
    void testUpdateBook() {
        Book newBook = new Book("My Book Original", null, null);
        newBook.setAuthorId(1L);

        Book savedBook = bookDao.saveNewBook(newBook);
        savedBook.setTitle("My Book Updated");

        Book updatedBook = bookDao.updateBook(savedBook);

        assertEquals("My Book Updated", updatedBook.getTitle());
    }

    @Test
    void testDeleteBook() {
        Book newBook = new Book("My Book 3", null, null);
        newBook.setAuthorId(1L);

        Book savedBook = bookDao.saveNewBook(newBook);

        bookDao.deleteBookById(savedBook.getId());

        assertNull(bookDao.getById(savedBook.getId()));
    }

}
