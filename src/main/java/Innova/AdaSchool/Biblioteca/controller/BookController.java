package Innova.AdaSchool.Biblioteca.controller;

import Innova.AdaSchool.Biblioteca.dto.BookDto;
import Innova.AdaSchool.Biblioteca.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public ResponseEntity<List<BookDto>> findAll(){
        List<BookDto> books = this.bookService.getAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable String id){
        BookDto book = this.bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDto>> findByAuthor(@PathVariable("author") String author){
        List<BookDto> books = this.bookService.getByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookDto>> findByTitle(@PathVariable("title") String title){
        List<BookDto> books = this.bookService.getByTitle(title);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/availability/{availability}")
    public ResponseEntity<List<BookDto>> findByAvailability(@PathVariable("availability") String availability){
        List<BookDto> books = this.bookService.getByAvailability(availability);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/")
    public ResponseEntity<BookDto> save(@RequestBody BookDto bookDto){
        BookDto saved = this.bookService.save(bookDto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@RequestBody BookDto bookDto, @PathVariable String id){
        BookDto updated = this.bookService.update(bookDto, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        String confirm = this.bookService.delete(id);
        return ResponseEntity.ok(confirm);
    }
}
