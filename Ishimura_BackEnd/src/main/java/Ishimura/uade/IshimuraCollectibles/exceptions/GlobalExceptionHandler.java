package Ishimura.uade.IshimuraCollectibles.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 1) Excepciones de dominio (negocio)
  @ExceptionHandler(DomainException.class)
  public ProblemDetail handleDomain(DomainException ex, HttpServletRequest req) {
    HttpStatus status = switch (ex.getCode()) {
      case CATEGORY_ALREADY_EXISTS, LINE_ALREADY_EXISTS, PROMOTION_CONFLICT,
          COLLECTIBLE_ALREADY_EXISTS, WISHLIST_ITEM_ALREADY_EXISTS ->
        HttpStatus.CONFLICT; // 409
      case CATEGORY_NOT_FOUND, BRAND_NOT_FOUND, LINE_NOT_FOUND, USER_NOT_FOUND,
          ORDER_NOT_FOUND, IMAGE_NOT_FOUND, COLLECTIBLE_NOT_FOUND, CATALOG_ITEM_NOT_FOUND ->
        HttpStatus.NOT_FOUND; // 404
      case INVALID_DOMAIN_STATE -> HttpStatus.UNPROCESSABLE_ENTITY; // 422
      default -> HttpStatus.UNPROCESSABLE_ENTITY;
    };
    return pd(status, titleFor(ex.getCode()), ex.getMessage(), ex.getCode().name(), req.getRequestURI(), null);
  }

  // 2) Bean Validation (@Valid) de los DTOs -> 400 con listado de campos
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setTitle("Error de validación");
    pd.setProperty("code", "VALIDATION_ERROR");
    pd.setProperty("instance", req.getRequestURI());
    pd.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
        .toList());
    return pd;
  }

  // 2.1) Argumentos inválidos o request mal formado -> 400
  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
    return pd(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage(), "BAD_REQUEST", req.getRequestURI(), null);
  }

  // 2.2) Entidad no encontrada por JPA -> 404
  @ExceptionHandler(EntityNotFoundException.class)
  public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
    return pd(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), "ENTITY_NOT_FOUND", req.getRequestURI(),
        null);
  }

  // 3) Seguridad
  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuth(AuthenticationException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    pd.setTitle("No autorizado");
    pd.setDetail("Credenciales inválidas. Verificá tu email y contraseña.");
    pd.setProperty("code", "AUTH_UNAUTHORIZED");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    pd.setTitle("Prohibido");
    pd.setDetail("No tenés permisos para realizar esta acción");
    pd.setProperty("code", "AUTH_FORBIDDEN");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 4) Constraints de DB no previstas -> 409
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    pd.setTitle("Conflicto de integridad de datos");
    pd.setDetail("La operación viola una restricción de la base de datos");
    pd.setProperty("code", "DATA_INTEGRITY_VIOLATION");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleIllegalStateException(IllegalStateException ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    pd.setDetail(ex.getMessage());
    pd.setProperty("code", "IllegalStateException");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 5) Fallback genérico -> 500
  @ExceptionHandler(RuntimeException.class)
  public ProblemDetail handleRuntime(RuntimeException ex, HttpServletRequest req) {
    String msg = ex.getMessage() != null ? ex.getMessage() : "";
    String lower = msg.toLowerCase();
    if (lower.contains("wishlist")) {
      return pd(HttpStatus.CONFLICT, titleFor(ErrorCode.WISHLIST_ITEM_ALREADY_EXISTS), msg,
          ErrorCode.WISHLIST_ITEM_ALREADY_EXISTS.name(), req.getRequestURI(), null);
    }
    if (lower.contains("coleccionable no encontrado")) {
      return pd(HttpStatus.NOT_FOUND, titleFor(ErrorCode.COLLECTIBLE_NOT_FOUND), msg,
          ErrorCode.COLLECTIBLE_NOT_FOUND.name(), req.getRequestURI(), null);
    }
    return handleUnexpected(ex, req);
  }

  // 6) Fallback genérico -> 500
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    pd.setTitle("Error inesperado");
    pd.setProperty("code", "UNEXPECTED_ERROR");
    pd.setProperty("instance", req.getRequestURI());
    return pd;
  }

  // 6) Multipart: parte requerida faltante (p.ej., 'file') -> 400
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ProblemDetail handleMissingPart(MissingServletRequestPartException ex, HttpServletRequest req) {
    String part = ex.getRequestPartName();
    return pd(HttpStatus.BAD_REQUEST,
        "Solicitud inválida",
        "Falta la parte requerida '" + part + "'",
        "BAD_REQUEST",
        req.getRequestURI(),
        null);
  }

  private ProblemDetail pd(HttpStatus status, String title, String detail,
      String code, String instance, Map<String, ?> extra) {
    var p = ProblemDetail.forStatus(status);
    p.setTitle(title);
    if (detail != null)
      p.setDetail(detail);
    p.setProperty("code", code);
    p.setProperty("instance", instance);
    if (extra != null && !extra.isEmpty())
      p.setProperty("errors", extra);
    return p;
  }

  private String titleFor(ErrorCode code) {
    return switch (code) {
      case CATEGORY_ALREADY_EXISTS -> "La categoría ya existe";
      case CATEGORY_NOT_FOUND -> "Categoría no encontrada";
      case LINE_ALREADY_EXISTS -> "La línea ya existe";
      case LINE_NOT_FOUND -> "Línea no encontrada";
      case BRAND_NOT_FOUND -> "Marca no encontrada";
      case USER_ALREADY_EXISTS -> "Usuario ya existe";
      case USER_NOT_FOUND -> "Usuario no encontrado";
      case ORDER_NOT_FOUND -> "Orden no encontrada";
      case IMAGE_NOT_FOUND -> "Imagen no encontrada";
      case COLLECTIBLE_NOT_FOUND -> "Coleccionable no encontrado";
      case COLLECTIBLE_ALREADY_EXISTS -> "Coleccionable duplicado";
      case CATALOG_ITEM_NOT_FOUND -> "Item de catálogo no encontrado";
      case PROMOTION_CONFLICT -> "Conflicto de promoción";
      case INVALID_DOMAIN_STATE -> "Estado de dominio inválido";
      case DATA_INTEGRITY_VIOLATION -> "Conflicto de integridad";
      case WISHLIST_ITEM_ALREADY_EXISTS -> "El item ya está en la wishlist";
      case AUTH_FORBIDDEN -> "Prohibido";
      case AUTH_UNAUTHORIZED -> "No autorizado";
      case AUTH_NOT_FOUND -> "Auth no encontrado";
      case VALIDATION_ERROR -> "Error de validación";
    };
  }
}
