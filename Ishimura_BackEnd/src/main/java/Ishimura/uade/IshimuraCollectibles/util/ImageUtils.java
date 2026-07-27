package Ishimura.uade.IshimuraCollectibles.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;

public final class ImageUtils {
  private ImageUtils() {}

  public static byte[] renderSquarePng(Blob blob, int size, Color background, int padding)
      throws IOException, java.sql.SQLException {
    if (size <= 0) throw new IllegalArgumentException("size debe ser > 0");
    if (padding < 0) throw new IllegalArgumentException("padding no puede ser negativo");
    int canvas = size;

    try (InputStream in = blob.getBinaryStream()) {
      BufferedImage src = ImageIO.read(in);
      if (src == null) {
        throw new IOException("Formato de imagen no soportado");
      }

      int target = Math.max(1, canvas - padding * 2);
      double scale = Math.min((double) target / src.getWidth(), (double) target / src.getHeight());
      int w = Math.max(1, (int) Math.round(src.getWidth() * scale));
      int h = Math.max(1, (int) Math.round(src.getHeight() * scale));

      BufferedImage out = new BufferedImage(canvas, canvas, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = out.createGraphics();
      try {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillRect(0, 0, canvas, canvas);
        int x = (canvas - w) / 2;
        int y = (canvas - h) / 2;
        g.drawImage(src, x, y, w, h, null);
      } finally {
        g.dispose();
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(out, "png", baos);
      return baos.toByteArray();
    }
  }

  public static Color parseHexColor(String hex, Color fallback) {
    if (hex == null || hex.isBlank()) return fallback;
    String s = hex.trim();
    if (s.startsWith("#")) s = s.substring(1);
    try {
      if (s.length() == 6) {
        int rgb = Integer.parseInt(s, 16);
        return new Color(rgb);
      } else if (s.length() == 8) { // ARGB
        int argb = (int) Long.parseLong(s, 16);
        return new Color((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, (argb >> 24) & 0xFF);
      }
    } catch (NumberFormatException ignored) {}
    return fallback;
  }
}

