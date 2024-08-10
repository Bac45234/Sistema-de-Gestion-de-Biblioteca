package Innova.AdaSchool.Biblioteca.dto;

import java.util.HashMap;

public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private HashMap<String, String> myBooks;

    public UserDto(Long id, String name, String email, String password, HashMap<String, String> myBooks) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.myBooks = myBooks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public HashMap<String, String> getMyBooks() { return myBooks; }
    public void setMyBooks(HashMap<String, String> myBooks) { this.myBooks = myBooks; }
}
