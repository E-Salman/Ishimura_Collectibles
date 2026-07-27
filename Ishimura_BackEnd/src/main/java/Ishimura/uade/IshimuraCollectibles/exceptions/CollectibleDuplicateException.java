package Ishimura.uade.IshimuraCollectibles.exceptions;

public class CollectibleDuplicateException extends DomainException {

  private final String nombre;
  private final Long lineaId;

  public CollectibleDuplicateException(String nombre, Long lineaId) {
    super(ErrorCode.COLLECTIBLE_ALREADY_EXISTS,
        "El coleccionable '" + nombre + "' ya existe en la línea id=" + lineaId);
    this.nombre = nombre;
    this.lineaId = lineaId;
  }

  public String getNombre() {
    return nombre;
  }

  public Long getLineaId() {
    return lineaId;
  }
}

