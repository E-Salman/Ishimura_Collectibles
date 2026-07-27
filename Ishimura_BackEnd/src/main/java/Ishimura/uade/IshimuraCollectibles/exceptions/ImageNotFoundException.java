package Ishimura.uade.IshimuraCollectibles.exceptions;

public class ImageNotFoundException extends DomainException {
  public ImageNotFoundException(Long id) {
    super(ErrorCode.IMAGE_NOT_FOUND, "Imagen no encontrada id=" + id);
  }
}

