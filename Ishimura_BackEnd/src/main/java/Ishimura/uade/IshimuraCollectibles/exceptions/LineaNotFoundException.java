package Ishimura.uade.IshimuraCollectibles.exceptions;

public class LineaNotFoundException extends DomainException {

  public LineaNotFoundException(Long id) {
    super(ErrorCode.LINE_NOT_FOUND, "Linea no encontrada id=" + id);
  }

  public LineaNotFoundException(String detail) {
    super(ErrorCode.LINE_NOT_FOUND, "Linea no encontrada: " + detail);
  }
}

