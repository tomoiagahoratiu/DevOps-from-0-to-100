package com.fortech.igrow;

import com.fortech.igrow.model.Book;
import com.fortech.igrow.service.BookService;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IgrowApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgrowApplication.class, args);
	}

	@Bean
	ApplicationRunner init(BookService bookService) {
		return args -> {
			Stream.of("Mogli", "Cenusareasa", "Heidi", "Winnetou").forEach(name -> {
				Book book = new Book();
				book.setName(name);
				bookService.saveBook(book);
			});
		};
	}
}
