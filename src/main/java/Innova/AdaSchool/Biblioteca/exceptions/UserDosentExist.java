package Innova.AdaSchool.Biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El usuario no existe")
public class UserDosentExist extends RuntimeException {

    public UserDosentExist() {
        super("El usuario no existe");
    }
}
