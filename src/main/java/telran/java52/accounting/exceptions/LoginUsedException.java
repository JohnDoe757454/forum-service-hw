package telran.java52.accounting.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LoginUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
