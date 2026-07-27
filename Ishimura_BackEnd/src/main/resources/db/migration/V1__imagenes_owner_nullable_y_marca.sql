-- Hacer opcional el coleccionable para soportar imágenes de marca
ALTER TABLE imagenes
  MODIFY coleccionable_id BIGINT NULL;

-- (Opcional) Enforce exclusividad de pertenencia: exactamente uno de los dos
-- Si tu MySQL no aplica CHECK, no falla; en 8.0.16+ se hace cumplir
ALTER TABLE imagenes
  ADD CONSTRAINT chk_imagenes_owner
  CHECK ((coleccionable_id IS NULL AND marca_id IS NOT NULL)
      OR (coleccionable_id IS NOT NULL AND marca_id IS NULL));

