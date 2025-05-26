import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import model.Author;
import model.Book;
import model.Publisher;
import util.JpaUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {

saveData();
getAllAuthorsBooks();
updateBook(1,"Java2");
deleteBook(1);

    }
    public static void getAllAuthorsBooks() {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            Query query = em.createQuery("SELECT a FROM Author a");
            List<Author> authors = query.getResultList();
            authors.forEach( a -> System.out.println(a.getName() + " - " + a.getBooks().toString()));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveData(){
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
           Author a = new Author("Goran");
           Author b = new Author("Milica");
           em.persist(a);
           em.persist(b);

            Book book1 = new Book("Java");
            Book book2 = new Book("C#");

            em.persist(book1);
            em.persist(book2);

            Publisher publisher = new Publisher("OOP");
            Publisher publisher2 = new Publisher("OOP2");

            em.persist(publisher);
            em.persist(publisher2);

            a.getBooks().add(book1);
            b.getBooks().add(book2);
            em.merge(a);
            em.merge(b);

            et.commit();
        }catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
    }
    public static void updateBook(long bookId , String newTitle) {

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
         et.begin();
         Book book = em.find(Book.class, bookId);
         if(book != null) {
             book.setTitle(newTitle);
             em.merge(book);
         }
            et.commit();
        }catch (Exception e) {
            et.rollback();
        }finally {
            em.close();
        }
    }
    public static void deleteBook(long bookId) {

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
         et.begin();
         Book book = em.find(Book.class, bookId);
         if(book != null) {
             em.remove(book);
         }
        }catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
        }finally {
            em.close();
        }
    }
}
