package Innova.AdaSchool.Biblioteca.service;

import Innova.AdaSchool.Biblioteca.exceptions.BookDosentExist;
import Innova.AdaSchool.Biblioteca.exceptions.BookNotAvailability;
import Innova.AdaSchool.Biblioteca.exceptions.BookNotInProperty;
import Innova.AdaSchool.Biblioteca.exceptions.UserDosentExist;
import Innova.AdaSchool.Biblioteca.dto.UserDto;
import Innova.AdaSchool.Biblioteca.entity.BookEntity;
import Innova.AdaSchool.Biblioteca.entity.UserEntity;
import Innova.AdaSchool.Biblioteca.repository.BookMongoRepository;
import Innova.AdaSchool.Biblioteca.repository.UserPostgreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserPostgreRepository userPostgreRepository;

    @Autowired
    private BookMongoRepository bookMongoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String email;

    public void setEmail(String email){ this.email = email; }

    public List<UserDto> getAll(){
        return this.userPostgreRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto getMyData(){
        return this.userPostgreRepository.findByEmail(this.email)
                .map(this::toDto)
                .orElseThrow(UserDosentExist::new);
    }

    public UserDto getById(Long id){
        UserEntity entity = this.userPostgreRepository.findById(id)
                .orElseThrow(UserDosentExist::new);
        return toDto(entity);
    }

    public UserDto update(UserDto request){
        UserEntity OldData = this.userPostgreRepository.findByEmail(this.email)
                .orElseThrow(UserDosentExist::new);

        UserEntity NewData = new UserEntity();
        NewData.setId(OldData.getId());
        NewData.setName(request.getName());
        NewData.setEmail(this.email);
        NewData.setPassword(passwordEncoder.encode((request.getPassword())));
        NewData.setMyBooks(OldData.getMyBooks());
        UserEntity entity = this.userPostgreRepository.save(NewData);
        return toDto(entity);
    }

    public String setNewBook(String book_id){
        BookEntity book = this.bookMongoRepository.findById(book_id)
                .orElseThrow(BookDosentExist::new);
        if (book.getAvailability().equals("No disponible")){
           throw new BookNotAvailability();
        }
        UserEntity user = this.userPostgreRepository.findByEmail(this.email)
                .orElseThrow(UserDosentExist::new);

        HashMap<String, String> books = user.getMyBooks();
        books.put(book.getId(), book.getTitle());
        this.userPostgreRepository.updateMyBooks(this.email, books);
        this.bookMongoRepository.updateAvailability(book_id, "No disponible");
        return "El libro '"+book.getTitle()+"' se ha agregado a su colección";
    }

    public String delete(Long id){
        this.userPostgreRepository.findById(id)
                .orElseThrow(UserDosentExist::new);

        this.userPostgreRepository.deleteById(id);
        return "El usuario se elimino correctamente";
    }

    public String restoreBook(String book_id){
        UserEntity user = this.userPostgreRepository.findByEmail(this.email)
                .orElseThrow(UserDosentExist::new);

        HashMap<String, String> books = user.getMyBooks();
        String deletedBook = books.get(book_id);
        if (deletedBook == null){
            throw new BookNotInProperty();
        }
        books.remove(book_id);
        this.userPostgreRepository.updateMyBooks(this.email, books);
        this.bookMongoRepository.updateAvailability(book_id, "Disponible");
        return "Has devuelto el libro '"+deletedBook+"'";
    }


    private UserDto toDto(UserEntity entity){
        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getMyBooks()
        );
    }
}
