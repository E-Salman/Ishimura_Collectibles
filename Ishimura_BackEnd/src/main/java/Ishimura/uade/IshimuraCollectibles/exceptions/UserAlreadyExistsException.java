package Ishimura.uade.IshimuraCollectibles.exceptions;

public class UserAlreadyExistsException extends DomainException {
  private final String email;

  public UserAlreadyExistsException(String email) {
    super(ErrorCode.USER_ALREADY_EXISTS, "El email '" + email + "' ya está registrado");
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}

