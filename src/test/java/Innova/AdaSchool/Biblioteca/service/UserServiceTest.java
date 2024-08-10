package Innova.AdaSchool.Biblioteca.service;

import Innova.AdaSchool.Biblioteca.entity.BookEntity;
import Innova.AdaSchool.Biblioteca.entity.UserEntity;
import Innova.AdaSchool.Biblioteca.dto.UserDto;
import Innova.AdaSchool.Biblioteca.exceptions.BookDosentExist;
import Innova.AdaSchool.Biblioteca.exceptions.BookNotAvailability;
import Innova.AdaSchool.Biblioteca.exceptions.BookNotInProperty;
import Innova.AdaSchool.Biblioteca.exceptions.UserDosentExist;
import Innova.AdaSchool.Biblioteca.repository.BookMongoRepository;
import Innova.AdaSchool.Biblioteca.repository.UserPostgreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;



public class UserServiceTest {

    @Mock
    private UserPostgreRepository userPostgreRepository;

    @Mock
    private BookMongoRepository bookMongoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService.setEmail("test@example.com");
    }

    @Test
    public void testGetAll() {
        // Arrange
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        when(userPostgreRepository.findAll()).thenReturn(List.of(userEntity));

        // Act
        var result = userService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    public void testGetMyData() {
        // Arrange
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        // Act
        var result = userService.getMyData();

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void testGetMyData_UserDoesNotExist() {
        // Arrange
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDosentExist.class, () -> userService.getMyData());
    }

    @Test
    public void testGetById() {
        // Arrange
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        when(userPostgreRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        // Act
        var result = userService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    public void testGetById_UserDoesNotExist() {
        // Arrange
        when(userPostgreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDosentExist.class, () -> userService.getById(1L));
    }

    @Test
    public void testUpdate() {
        // Arrange
        UserEntity oldUser = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        UserDto newUserDto = new UserDto(1L, "John Updated", "test@example.com", "newpassword", new HashMap<>());
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.of(oldUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userPostgreRepository.save(any(UserEntity.class))).thenReturn(new UserEntity(1L, "John Updated", "test@example.com", "encodedpassword", new HashMap<>()));

        // Act
        var result = userService.update(newUserDto);

        // Assert
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("encodedpassword", result.getPassword());
    }

    @Test
    public void testUpdate_UserDoesNotExist() {
        // Arrange
        UserDto userDto = new UserDto(1L, "John Updated", "test@example.com", "newpassword", new HashMap<>());
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDosentExist.class, () -> userService.update(userDto));
    }

    @Test
    public void testDelete() {
        // Arrange
        when(userPostgreRepository.findById(anyLong())).thenReturn(Optional.of(new UserEntity()));

        // Act
        String result = userService.delete(1L);

        // Assert
        assertEquals("El usuario se elimino correctamente", result);
    }

    @Test
    public void testDelete_UserDoesNotExist() {
        // Arrange
        when(userPostgreRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserDosentExist.class, () -> userService.delete(1L));
    }

    @Test
    public void testSetNewBook() {
        // Arrange
        BookEntity bookEntity = new BookEntity("book1", "Author", "Title", "Disponible");
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        when(bookMongoRepository.findById(anyString())).thenReturn(Optional.of(bookEntity));
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(userPostgreRepository).updateMyBooks(anyString(), any(HashMap.class));
        doNothing().when(bookMongoRepository).updateAvailability(anyString(), anyString());

        // Act
        String result = userService.setNewBook("book1");

        // Assert
        assertEquals("El libro 'Title' se ha agregado a su colección", result);
    }

    @Test
    public void testSetNewBook_BookDoesNotExist() {
        // Arrange
        when(bookMongoRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookDosentExist.class, () -> userService.setNewBook("book1"));
    }

    @Test
    public void testSetNewBook_BookNotAvailable() {
        // Arrange
        BookEntity bookEntity = new BookEntity("book1", "Author", "Title", "No disponible");
        when(bookMongoRepository.findById(anyString())).thenReturn(Optional.of(bookEntity));

        // Act & Assert
        assertThrows(BookNotAvailability.class, () -> userService.setNewBook("book1"));
    }

    @Test
    public void testRestoreBook() {
        // Arrange
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        userEntity.getMyBooks().put("book1", "Title");
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));
        doNothing().when(userPostgreRepository).updateMyBooks(anyString(), any(HashMap.class));
        doNothing().when(bookMongoRepository).updateAvailability(anyString(), anyString());

        // Act
        String result = userService.restoreBook("book1");

        // Assert
        assertEquals("Has devuelto el libro 'Title'", result);
    }

    @Test
    public void testRestoreBook_BookNotInProperty() {
        // Arrange
        UserEntity userEntity = new UserEntity(1L, "John", "test@example.com", "password", new HashMap<>());
        when(userPostgreRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        // Act & Assert
        assertThrows(BookNotInProperty.class, () -> userService.restoreBook("book1"));
    }
}
