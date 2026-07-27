package Ishimura.uade.IshimuraCollectibles.exceptions;

public class MarcaNotFoundException extends DomainException {

  public MarcaNotFoundException(Long id) {
    super(ErrorCode.BRAND_NOT_FOUND, "Marca no encontrada id=" + id);
  }

  public MarcaNotFoundException(String detail) {
    super(ErrorCode.BRAND_NOT_FOUND, "Marca no encontrada: " + detail);
  }
}

