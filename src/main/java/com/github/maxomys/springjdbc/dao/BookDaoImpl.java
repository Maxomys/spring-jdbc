package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;
import com.github.maxomys.springjdbc.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao {

    private final DataSource source;

    public BookDaoImpl(DataSource source) {
        this.source = source;
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
    public Book saveNewBook(Book book) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            statement.setLong(4, book.getAuthorId());

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
            statement.setLong(4, book.getAuthorId());
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
                book.setAuthorId(resultSet.getLong("author_id"));

                return book;
            }
        }
        return null;
    }

}
