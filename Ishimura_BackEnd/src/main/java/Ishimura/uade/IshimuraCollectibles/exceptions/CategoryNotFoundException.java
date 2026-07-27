package Ishimura.uade.IshimuraCollectibles.exceptions;

public class CategoryNotFoundException extends DomainException {

  public CategoryNotFoundException(Long id) {
    super(ErrorCode.CATEGORY_NOT_FOUND, "Categoria no encontrada id=" + id);
  }

  public CategoryNotFoundException(String description) {
    super(ErrorCode.CATEGORY_NOT_FOUND, "Categoria no encontrada: '" + description + "'");
  }
}
