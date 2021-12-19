package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
@Primary
public class BookDaoImpl implements BookDao {

    private final DataSource source;

    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource source, AuthorDao authorDao) {
        this.source = source;
        this.authorDao = authorDao;
    }

    @Override
    public Book getById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE id = ?")) {

            statement.setLong(1, id);

            return createBook(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Book findBookByTitle(String title) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM book WHERE title = ?")) {

            statement.setString(1, title);

            return createBook(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Book findByISBN(String isbn) {
        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                statement.setLong(4, book.getAuthor().getId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }

            statement.executeUpdate();

            try (Statement resultStatement = connection.createStatement();
                 ResultSet resultSet = resultStatement.executeQuery("SELECT LAST_INSERT_ID()")) {

                if (resultSet.next()) {
                    return getById(resultSet.getLong(1));
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Book updateBook(Book book) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE book.id = ?")) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                statement.setLong(4, book.getAuthor().getId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }
            statement.setLong(5, book.getId());

            statement.executeUpdate();

            return getById(book.getId());

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteBookById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM book WHERE book.id = ?")) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Book createBook(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Book book = new Book();

                book.setId(resultSet.getLong("id"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setTitle(resultSet.getString("title"));
                if (resultSet.getLong("author_id") != 0) {
                    book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));
                }

                return book;
            }
        }
        return null;
    }

}
