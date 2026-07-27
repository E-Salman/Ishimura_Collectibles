package Ishimura.uade.IshimuraCollectibles.exceptions;

public class CollectibleNotFoundException extends DomainException {
  public CollectibleNotFoundException(Long id) {
    super(ErrorCode.COLLECTIBLE_NOT_FOUND, "Coleccionable no encontrado id=" + id);
  }

  public CollectibleNotFoundException(String detail) {
    super(ErrorCode.COLLECTIBLE_NOT_FOUND, "Coleccionable no encontrado: " + detail);
  }
}
