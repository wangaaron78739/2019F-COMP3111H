package arena.ui;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;

import java.io.InputStream;

import static arena.logic.ArenaConstants.*;

/**
 * Utility class for UI
 * @author Aaron WANG
 */
public class ArenaUIUtils {

    /**
     * Get image from resource
     * @param path path of image
     * @return Image object of desired image
     */
    static Image getImage(String path) {
        InputStream is = ArenaUIUtils.class.getResourceAsStream(path);
        return new Image(is);
    }

    /**
     * Get image view from image
     * @param i Image object of desired image
     * @return ImageView object of desired image
     */
    static ImageView setIcon(Image i) {
        ImageView iv = new ImageView(i);
        iv.setFitHeight(GRID_HEIGHT);
        iv.setFitWidth(GRID_WIDTH);
        return iv;
    }

    /**
     * Get image view object of certain height
     * @param i Image object of desired image
     * @param height Desired height of ImageView object
     * @param width Desired width of ImageView object
     * @return ImageView object of desired image
     */
    static ImageView setIcon(Image i, int height, int width) {
        ImageView iv = new ImageView(i);
        iv.setFitHeight(height);
        iv.setFitWidth(width);
        return iv;
    }

    /**
     * Default constructor for ArenaUIUtils
     */
    public ArenaUIUtils() {
    }
}
