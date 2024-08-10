package Innova.AdaSchool.Biblioteca.exceptions;

public class BookNotAvailability extends RuntimeException{

    public BookNotAvailability(){
        super("El libro que intento solicitar no esta disponible");
    }
}
