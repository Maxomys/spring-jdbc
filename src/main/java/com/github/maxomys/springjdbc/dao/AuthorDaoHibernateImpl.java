package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Author;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Component("authorDaoHibernate")
public class AuthorDaoHibernateImpl implements AuthorDao {

    private final EntityManagerFactory emf;

    public AuthorDaoHibernateImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getById(Long id) {
        return getEntityManager().find(Author.class, id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        //TypedQuery<Author> query = getEntityManager().createQuery("SELECT a FROM Author a " +
        //        "WHERE a.firstName = :first_name AND a.lastName = :last_name", Author.class);

        TypedQuery<Author> query = getEntityManager().createNamedQuery("find_by_name", Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);

        return query.getSingleResult();
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastname) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a WHERE a.lastName LIKE :last_name", Author.class);
            query.setParameter("last_name", lastname + "%");
            List<Author> authors = query.getResultList();
            return authors;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Author> findAllAuthors() {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Author> query = em.createNamedQuery("author_find_all", Author.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();

        return getById(author.getId());
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.merge(author);
        em.flush();
        em.getTransaction().commit();

        return getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
