package Innova.AdaSchool.Biblioteca.exceptions;

public class BookNotInProperty extends RuntimeException{

    public BookNotInProperty(){
        super("El libro que intento devolver no se encuentra en su propiedad");
    }
}
