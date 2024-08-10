package Innova.AdaSchool.Biblioteca.service;

import Innova.AdaSchool.Biblioteca.exceptions.BookDosentExist;
import Innova.AdaSchool.Biblioteca.dto.BookDto;
import Innova.AdaSchool.Biblioteca.entity.BookEntity;
import Innova.AdaSchool.Biblioteca.repository.BookMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookMongoRepository bookMongoRepository;

    public List<BookDto> getAll(){
        return this.bookMongoRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public BookDto getById(String id){
        return this.bookMongoRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(BookDosentExist::new);
    }

    public List<BookDto> getByAuthor(String author){
        return this.bookMongoRepository.findByAuthor(author)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<BookDto> getByTitle(String title){
        return this.bookMongoRepository.findByTitle(title)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<BookDto> getByAvailability(String availability){
        return this.bookMongoRepository.findByAvailability(availability)
                .stream()
                .map(this::toDto)
                .toList();
    }


    public BookDto save(BookDto book){
        BookEntity entity = new BookEntity();
        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setAvailability("Disponible");
        BookEntity entitySaved = this.bookMongoRepository.save(entity);
        return this.toDto(entitySaved);
    }

    public BookDto update(BookDto book, String id){
        BookEntity entity = this.bookMongoRepository.findById(id)
                .orElseThrow(BookDosentExist::new);
        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        BookEntity entitySaved = this.bookMongoRepository.save(entity);
        return this.toDto(entitySaved);
    }

    public String delete(String id){
        BookEntity entity = this.bookMongoRepository.findById(id)
                .orElseThrow(BookDosentExist::new);
        this.bookMongoRepository.delete(entity);
        return "Libro eliminado exitosamente";
    }


    private BookDto toDto(BookEntity entity){
        return new BookDto(
                entity.getId(),
                entity.getAuthor(),
                entity.getTitle(),
                entity.getAvailability()
        );
    }
}
