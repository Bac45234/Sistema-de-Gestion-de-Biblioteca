package Innova.AdaSchool.Biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El libro no existe")
public class BookDosentExist extends RuntimeException{

    public BookDosentExist(){
        super("El libro no existe");
    }
}
