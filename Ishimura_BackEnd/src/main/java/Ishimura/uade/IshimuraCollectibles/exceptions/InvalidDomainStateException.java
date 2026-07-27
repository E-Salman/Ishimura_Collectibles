package Ishimura.uade.IshimuraCollectibles.exceptions;

public class InvalidDomainStateException extends DomainException {
  public InvalidDomainStateException(String message) {
    super(ErrorCode.INVALID_DOMAIN_STATE, message);
  }
}

