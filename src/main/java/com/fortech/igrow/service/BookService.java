package com.fortech.igrow.service;

import com.fortech.igrow.model.Book;
import com.fortech.igrow.repository.BookRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@GraphQLApi
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @GraphQLQuery(name = "books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @GraphQLQuery(name = "book")
    public Optional<Book> getBookById(@GraphQLArgument(name = "id") Long id) {
        return bookRepository.findById(id);
    }

    @GraphQLMutation(name = "saveBook")
    public Book saveBook(@GraphQLArgument(name = "book") Book book) {
        return bookRepository.save(book);
    }

    @GraphQLMutation(name = "deleteBook")
    public Long deleteBook(@GraphQLArgument(name = "id") Long id) {
        bookRepository.deleteById(id);
        return id;
    }

    @GraphQLQuery(name = "isAvailable")
    public boolean isAvailable(@GraphQLContext Book book) {
        return Arrays.asList("Mogli", "Cenusareasa").contains(book.getName());
    }
}
