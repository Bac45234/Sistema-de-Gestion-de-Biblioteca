package Innova.AdaSchool.Biblioteca.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
public class BookEntity {

    @Id
    private String id;
    private String author;
    private String title;
    private String availability;

    public BookEntity(String id, String author, String title, String availability) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.availability = availability;
    }

    public BookEntity(){}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}
