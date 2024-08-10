package Innova.AdaSchool.Biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El usuario ya existe")
public class UserAlreadyExist extends RuntimeException{

    public UserAlreadyExist(){
        super("El usuario ya existe");
    }
}
