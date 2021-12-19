package com.github.maxomys.springjdbc.dao;

import com.github.maxomys.springjdbc.domain.Book;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

@Component("bookDaoHibernate")
public class BookDaoHibernateImpl implements BookDao {

    private final EntityManagerFactory emf;

    public BookDaoHibernateImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Book getById(Long id) {
        return getEntityManager().find(Book.class, id);
    }

    @Override
    public Book findBookByTitle(String title) {
        TypedQuery<Book> query = getEntityManager().createQuery("SELECT b FROM Book b " +
                "WHERE b.title = :title", Book.class);
        query.setParameter("title", title);

        return query.getSingleResult();
    }

    @Override
    public Book findByISBN(String isbn) {
        EntityManager em = getEntityManager();

        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();

        return getById(book.getId());
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.getTransaction().commit();

        return getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.flush();
        em.getTransaction().commit();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
