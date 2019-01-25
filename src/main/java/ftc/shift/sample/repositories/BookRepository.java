package ftc.shift.sample.repositories;

import ftc.shift.sample.models.Book;

import java.util.Collection;

/**
 * Интерфейс для получения данных по книгам
 */
public interface BookRepository {

    Book fetchBook(String id);

    Book updateBook(Book book);

    void deleteBook(String id);

    Book createBook(Book book);

    Collection<Book> getAllBooks();
}
