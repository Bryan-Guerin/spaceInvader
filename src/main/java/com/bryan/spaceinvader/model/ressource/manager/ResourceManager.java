package com.bryan.spaceinvader.model.ressource.manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {

    private static final Logger logger = LogManager.getLogger(ResourceManager.class);
    private static final String BASE_PATH = "/com/bryan/spaceinvader/";
    private static final Image defaultImage = getDefaultImage();
    public static final int IMAGE_CACHE_SIZE = 20;
    public static final int AUDIO_CACHE_SIZE = 20;

    private static final Map<String, Image> cache_image = new HashMap<>();
    private static final Map<String, Media> cache_audio = new HashMap<>();

    /**
     * Generic méthod to load a resource based on the resourceType.
     * <br> <br>
     * Can handle the following type :
     * <ul>
     *     <li>{@link Image} for images</li>
     *     <li>{@link Media} for wav file (audio)</li>
     *     <li>{@link FXMLLoader} for fxml file (scene)</li>
     * </ul>
     * <p>
     * If the file isn't found, it might load nothing.
     *
     * @param fileName     The name of the file INCLUDING the extension
     * @param resourceType see {@link ResourceType}
     * @return Return the loaded resource as a T type.
     */
    public static <T> T loadResource(String fileName, Class<T> type, ResourceType resourceType) {
        String fullPath = computeFullPath(fileName, resourceType);

        if (logger.isTraceEnabled())
            logger.trace("Loading resource : {} as {}", fullPath, resourceType.name());

        T object;

        if (type.equals(Image.class)) {
            if (cache_image.containsKey(fullPath)) {
                object = type.cast(cache_image.get(fullPath));
            } else {
                try {
                    URL url = ResourceManager.class.getResource(fullPath);
                    object = type.cast(Objects.isNull(url) ? defaultImage : new Image(url.toString()));
                    cache_image.put(fullPath, (Image) object);
                } catch (NullPointerException e) {
                    logger.error("File {} not found !", fullPath, e);
                    throw new AssertionError("File " + fullPath + " not found !");
                }
            }
            if (cache_image.size() > IMAGE_CACHE_SIZE)
                cache_image.clear();
        } else if (type.equals(Media.class)) { // TODO check if it works
            object = type.cast(ResourceManager.class.getResourceAsStream(fullPath));
            cache_audio.put(fullPath, (Media) object);
            if (cache_audio.size() > AUDIO_CACHE_SIZE)
                cache_audio.clear();
        } else if (type.equals(FXMLLoader.class)) {
            object = type.cast(new FXMLLoader(ResourceManager.class.getResource(fullPath)));
        } else if (type.equals(Parent.class)) {
            try {
                object = type.cast(new FXMLLoader(ResourceManager.class.getResource(fullPath)).load());
            } catch (IOException | NullPointerException e) {
                logger.error("File {} not found !", fullPath, e);
                throw new AssertionError("File " + fullPath + " not found !");
            }
        } else {
            throw new IllegalArgumentException("Unsupported resource type: " + type.getName());
        }

        if (logger.isTraceEnabled())
            logger.trace("Successfully load resource : {} as {}", fullPath, resourceType.name());

        return object;
    }

    private static String computeFullPath(String fileName, ResourceType resourceType) {
        return BASE_PATH + resourceType.getPathPrefix() + fileName;
    }

    private static Image getDefaultImage() {
        return new Image(Objects.requireNonNull(ResourceManager.class.getResource(computeFullPath("unknown.png", ResourceType.IMAGE))).toString());
    }
}