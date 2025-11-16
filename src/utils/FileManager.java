package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileManager {
    private static final String IMAGES_DIR = "resources/images/";

    /**
     * Проверить, существует ли файл изображения
     */
    public static boolean imageExists(String filename) {
        File file = new File(IMAGES_DIR + filename);
        return file.exists() && file.isFile();
    }

    /**
     * Получить полный путь к изображению
     */
    public static String getImagePath(String filename) {
        return IMAGES_DIR + filename;
    }

    /**
     * Загрузить изображение с проверкой
     */
    public static BufferedImage loadImage(String filename) {
        try {
            File file = new File(getImagePath(filename));
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке изображения: " + e.getMessage());
        }
        return null;
    }

    /**
     * Создать директорию, если её нет
     */
    public static void createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Сохранить изображение
     */
    public static boolean saveImage(BufferedImage image, String filename) {
        try {
            createDirectoryIfNotExists(IMAGES_DIR);
            File file = new File(getImagePath(filename));
            ImageIO.write(image, "png", file);
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении изображения: " + e.getMessage());
            return false;
        }
    }
}
