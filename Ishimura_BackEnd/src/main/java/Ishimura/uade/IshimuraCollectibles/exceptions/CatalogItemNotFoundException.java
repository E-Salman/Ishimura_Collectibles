package Ishimura.uade.IshimuraCollectibles.exceptions;

public class CatalogItemNotFoundException extends DomainException {
  public CatalogItemNotFoundException(Long id) {
    super(ErrorCode.CATALOG_ITEM_NOT_FOUND, "Item de catálogo no encontrado id=" + id);
  }
}

