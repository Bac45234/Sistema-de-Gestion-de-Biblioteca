package Innova.AdaSchool.Biblioteca.dto;

public class BookDto {

    private String id;
    private String author;
    private String title;
    private String availability;

    public BookDto(String id, String author, String title, String availability) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.availability = availability;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}
