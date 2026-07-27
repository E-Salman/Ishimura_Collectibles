package Ishimura.uade.IshimuraCollectibles.exceptions;

public class OrderNotFoundException extends DomainException {
  public OrderNotFoundException(String numeroOrden) {
    super(ErrorCode.ORDER_NOT_FOUND, "Orden no encontrada: " + numeroOrden);
  }
}

