package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component("jdbc")
@Primary
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource source;

    public AuthorDaoImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public Author getById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM author WHERE id = ?")) {

            statement.setLong(1, id);

            return createAuthor(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM author WHERE first_name = ? AND last_name = ?")) {

            statement.setString(1, firstName);
            statement.setString(2, lastName);

            return createAuthor(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)")) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

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
    public Author updateAuthor(Author author) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE author SET first_name = ?, last_name = ? WHERE author.id = ?")) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setLong(3, author.getId());

            statement.executeUpdate();

            return getById(author.getId());

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteAuthorById(Long id) {
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM author WHERE author.id = ?")) {

            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Author createAuthor(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Author author = new Author();

                author.setId(resultSet.getLong("id"));
                author.setFirstName(resultSet.getString("first_name"));
                author.setLastName(resultSet.getString("last_name"));

                return author;
            }
        }
        return null;
    }

}
