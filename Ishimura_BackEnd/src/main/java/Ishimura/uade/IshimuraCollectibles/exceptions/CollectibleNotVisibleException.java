package Ishimura.uade.IshimuraCollectibles.exceptions;

public class CollectibleNotVisibleException extends DomainException {
    public CollectibleNotVisibleException() {
        super(ErrorCode.COLLECTIBLE_NOT_FOUND, "No se encontro el coleccionable");
    }
}
