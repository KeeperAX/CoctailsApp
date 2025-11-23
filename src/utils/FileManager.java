package utils;                                               // Объявление пакета — класс относится к вспомогательным утилитам

import javax.imageio.ImageIO;                                 // Импорт класса для чтения и записи изображений в разных форматах
import java.awt.image.BufferedImage;                        // Импорт типа изображения, используемого в Swing и AWT
import java.io.File;                                          // Импорт класса для работы с файлами и директориями
import java.io.IOException;                                  // Импорт исключения, возникающего при ошибках ввода-вывода

public class FileManager {                                   // Публичный утилитарный класс для работы с изображениями коктейлей

    private static final String IMAGES_DIR = "resources/images/";  // Константа: путь к папке, где хранятся все изображения

    /**
     * Проверить, существует ли файл изображения по имени
     * @param filename имя файла (например, "martini.png")
     * @return true — если файл существует и является обычным файлом
     */
    public static boolean imageExists(String filename) {
        File file = new File(IMAGES_DIR + filename);         // Создаём объект File с полным путём к изображению
        return file.exists() && file.isFile();               // Проверяем: существует ли файл и является ли он файлом (не папкой)
    }

    /**
     * Получить полный путь к файлу изображения
     * @param filename имя файла без пути
     * @return строка с полным путём, например: "resources/images/martini.png"
     */
    public static String getImagePath(String filename) {
        return IMAGES_DIR + filename;                        // Просто соединяем путь к папке и имя файла
    }

    /**
     * Загрузить изображение из файла
     * @param filename имя файла изображения
     * @return объект BufferedImage, если загрузка прошла успешно, иначе — null
     */
    public static BufferedImage loadImage(String filename) {
        try {                                                // Начинаем обработку возможных ошибок ввода-вывода
            File file = new File(getImagePath(filename));    // Формируем полный путь к файлу
            if (file.exists()) {                             // Проверяем, существует ли файл на диске
                return ImageIO.read(file);                   // Читаем изображение и возвращаем как BufferedImage
            }
        } catch (IOException e) {                            // Ловим исключение при ошибке чтения (повреждённый файл, нет доступа и т.д.)
            System.out.println("Ошибка при загрузке изображения: " + e.getMessage());  // Выводим сообщение в консоль
        }
        return null;                                         // Если файл не найден или произошла ошибка — возвращаем null
    }

    /**
     * Создать директорию, если её ещё нет
     * @param dirPath полный путь к директории (например, "resources/images/")
     */
    public static void createDirectoryIfNotExists(String dirPath) {
        File dir = new File(dirPath);                        // Создаём объект директории
        if (!dir.exists()) {                                 // Проверяем, существует ли она
            dir.mkdirs();                                    // Если нет — создаём (mkdirs() создаёт все вложенные папки)
        }
    }

    /**
     * Сохранить BufferedImage как PNG-файл
     * @param image изображение для сохранения
     * @param filename имя файла (например, "new_cocktail.png")
     * @return true — если сохранение прошло успешно, false — если произошла ошибка
     */
    public static boolean saveImage(BufferedImage image, String filename) {
        try {                                                // Обработка ошибок записи
            createDirectoryIfNotExists(IMAGES_DIR);         // Убеждаемся, что папка существует
            File file = new File(getImagePath(filename));    // Формируем полный путь к новому файлу
            ImageIO.write(image, "png", file);               // Записываем изображение в формате PNG
            return true;                                     // Успешно сохранено
        } catch (IOException e) {                            // Ловим ошибку (нет прав, диск переполнен и т.д.)
            System.out.println("Ошибка при сохранении изображения: " + e.getMessage());  // Сообщаем об ошибке
            return false;                                    // Сохранение не удалось
        }
    }
}