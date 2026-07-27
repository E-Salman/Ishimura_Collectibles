package Ishimura.uade.IshimuraCollectibles.exceptions;

public abstract class DomainException extends RuntimeException {
  private final ErrorCode code;

  public DomainException(ErrorCode code, String message) {
    super(message);
    this.code = code;
  }

  public ErrorCode getCode() {
    return code;
  }
}
