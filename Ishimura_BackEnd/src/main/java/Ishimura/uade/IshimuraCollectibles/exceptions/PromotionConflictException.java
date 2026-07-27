package Ishimura.uade.IshimuraCollectibles.exceptions;

public class PromotionConflictException extends DomainException {
  public PromotionConflictException(Long coleccionableId) {
    super(ErrorCode.PROMOTION_CONFLICT, "Ya existe una promoción activa para el coleccionable id=" + coleccionableId);
  }
}

