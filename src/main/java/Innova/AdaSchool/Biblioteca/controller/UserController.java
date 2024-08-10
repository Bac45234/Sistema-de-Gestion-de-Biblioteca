package Innova.AdaSchool.Biblioteca.controller;

import Innova.AdaSchool.Biblioteca.dto.UserDto;
import Innova.AdaSchool.Biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyData(){
        UserDto meData = this.userService.getMyData();
        return ResponseEntity.ok(meData);
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<String> requestBook(@PathVariable("id") String book_id){
        String confirm = this.userService.setNewBook(book_id);
        return ResponseEntity.ok(confirm);
    }

    @DeleteMapping("/restore/{id}")
    public ResponseEntity<String> restoreBook(@PathVariable("id") String book_id){
        String status = this.userService.restoreBook(book_id);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> users = this.userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id){
        UserDto user = this.userService.getById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/")
    public ResponseEntity<UserDto> update(@RequestBody UserDto request){
        UserDto saved = this.userService.update(request);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        String status = this.userService.delete(id);
        return ResponseEntity.ok(status);
    }
}
