package Ishimura.uade.IshimuraCollectibles.exceptions;

public class UserNotFoundException extends DomainException {

  public UserNotFoundException(Long id) {
    super(ErrorCode.USER_NOT_FOUND, "Usuario no encontrado id=" + id);
  }

  public UserNotFoundException(String email) {
    super(ErrorCode.USER_NOT_FOUND, "Usuario no encontrado email=" + email);
  }
}

