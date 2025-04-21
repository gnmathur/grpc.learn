package dev.gmathur.sec03;

import dev.gmathur.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec05Collection {
    private static final Logger LOG = LoggerFactory.getLogger(Lec05Collection.class);

    public static void main(String[] args) {
        var book1 = Book.newBuilder()
                .setTitle("Algorithms")
                .setAuthor("Cormen")
                .setPublicationYear(2021)
                .build();

        var book2 = Book.newBuilder()
                .setTitle("Design Patterns")
                .setAuthor("Gang of Four")
                .setPublicationYear(2020)
                .build();

        var book3 = Book.newBuilder()
                .setTitle("Harry Potter - The Philosopher's Stone")
                .setAuthor("J.K. Rowling")
                .setPublicationYear(1997)
                .build();

        // more books
        var book4 = book3.toBuilder().setTitle("Harry Potter - The Chamber of Secrets").setPublicationYear(1999).build();
        var book5 = book3.toBuilder().setTitle("Harry Potter - The Prisoner of Azkaban").setPublicationYear(2001).build();
        var book6 = book3.toBuilder().setTitle("Harry Potter - The Goblet of Fire").setPublicationYear(2002).build();

        var library = Library.newBuilder()
                .addBooks(book1)
                .addBooks(book2)
                .addBooks(book3)
                .addBooks(book4)
                .addBooks(book5)
                .addBooks(book6)
                .build();

        LOG.info("Library: {}", library);

        // Another way to add books
        var library2 = Library.newBuilder().addAllBooks(List.of(book1, book2, book3, book4, book5, book6)).build();
        LOG.info("Library2: {}", library2);

        // get specific things
        library2.getBooksList().forEach(b -> LOG.info("Book: {}", b));
        LOG.info("Library2 Book Count: {}", library2.getBooksCount());
        LOG.info("Library1 Book Count: {}", library.getBooksCount());
        // etc.
    }
}
