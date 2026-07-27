package Ishimura.uade.IshimuraCollectibles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ColeccionableException extends RuntimeException {
    public ColeccionableException(String message) {
        super(message);
    }
}
