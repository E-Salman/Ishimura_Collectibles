package Ishimura.uade.IshimuraCollectibles.exceptions;

public class WishlistItemAlreadyExistsException extends DomainException {

  public WishlistItemAlreadyExistsException(String nombreColeccionable) {
    super(ErrorCode.WISHLIST_ITEM_ALREADY_EXISTS,
        "El coleccionable " + nombreColeccionable + " ya está en tu wishlist");
  }
}