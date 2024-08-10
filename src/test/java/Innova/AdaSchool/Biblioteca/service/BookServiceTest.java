package Innova.AdaSchool.Biblioteca.service;

import Innova.AdaSchool.Biblioteca.dto.BookDto;
import Innova.AdaSchool.Biblioteca.entity.BookEntity;
import Innova.AdaSchool.Biblioteca.exceptions.BookDosentExist;
import Innova.AdaSchool.Biblioteca.repository.BookMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookMongoRepository bookMongoRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        BookEntity bookEntity1 = new BookEntity("1", "Author1", "Title1", "Disponible");
        BookEntity bookEntity2 = new BookEntity("2", "Author2", "Title2", "No Disponible");
        List<BookEntity> bookEntities = List.of(bookEntity1, bookEntity2);

        when(bookMongoRepository.findAll()).thenReturn(bookEntities);

        List<BookDto> result = bookService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Author1", result.get(0).getAuthor());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Disponible", result.get(0).getAvailability());
    }

    @Test
    void testGetById_Found() {
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");

        when(bookMongoRepository.findById("1")).thenReturn(Optional.of(bookEntity));

        BookDto result = bookService.getById("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Author", result.getAuthor());
        assertEquals("Title", result.getTitle());
        assertEquals("Disponible", result.getAvailability());
    }

    @Test
    void testGetById_NotFound() {
        when(bookMongoRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookDosentExist.class, () -> bookService.getById("1"));
    }

    @Test
    void testGetByAuthor() {
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");
        List<BookEntity> bookEntities = List.of(bookEntity);

        when(bookMongoRepository.findByAuthor("Author")).thenReturn(bookEntities);

        List<BookDto> result = bookService.getByAuthor("Author");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Author", result.get(0).getAuthor());
        assertEquals("Title", result.get(0).getTitle());
        assertEquals("Disponible", result.get(0).getAvailability());
    }

    @Test
    void testGetByTitle() {
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");
        List<BookEntity> bookEntities = List.of(bookEntity);

        when(bookMongoRepository.findByTitle("Title")).thenReturn(bookEntities);

        List<BookDto> result = bookService.getByTitle("Title");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Author", result.get(0).getAuthor());
        assertEquals("Title", result.get(0).getTitle());
        assertEquals("Disponible", result.get(0).getAvailability());
    }

    @Test
    void testGetByAvailability() {
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");
        List<BookEntity> bookEntities = List.of(bookEntity);

        when(bookMongoRepository.findByAvailability("Disponible")).thenReturn(bookEntities);

        List<BookDto> result = bookService.getByAvailability("Disponible");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Author", result.get(0).getAuthor());
        assertEquals("Title", result.get(0).getTitle());
        assertEquals("Disponible", result.get(0).getAvailability());
    }

    @Test
    void testSaveBook() {
        BookDto bookDto = new BookDto("1", "Author", "Title", "Disponible");
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");

        when(bookMongoRepository.save(any(BookEntity.class))).thenReturn(bookEntity);

        BookDto result = bookService.save(bookDto);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Author", result.getAuthor());
        assertEquals("Title", result.getTitle());
        assertEquals("Disponible", result.getAvailability());
    }

    @Test
    void testUpdateBook_Success() {
        BookDto bookDto = new BookDto("1", "New Author", "New Title", "Disponible");
        BookEntity bookEntity = new BookEntity("1", "Old Author", "Old Title", "Disponible");
        BookEntity updatedBookEntity = new BookEntity("1", "New Author", "New Title", "Disponible");

        when(bookMongoRepository.findById("1")).thenReturn(Optional.of(bookEntity));
        when(bookMongoRepository.save(any(BookEntity.class))).thenReturn(updatedBookEntity);

        BookDto result = bookService.update(bookDto, "1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("New Author", result.getAuthor());
        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testUpdateBook_NotFound() {
        BookDto bookDto = new BookDto("1", "Author", "Title", "Disponible");

        when(bookMongoRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookDosentExist.class, () -> bookService.update(bookDto, "1"));
    }

    @Test
    void testDeleteBook_Success() {
        BookEntity bookEntity = new BookEntity("1", "Author", "Title", "Disponible");

        when(bookMongoRepository.findById("1")).thenReturn(Optional.of(bookEntity));

        String result = bookService.delete("1");
        assertEquals("Libro eliminado exitosamente", result);
        verify(bookMongoRepository, times(1)).delete(bookEntity);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookMongoRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookDosentExist.class, () -> bookService.delete("1"));
        verify(bookMongoRepository, times(0)).delete(any(BookEntity.class));
    }
}
