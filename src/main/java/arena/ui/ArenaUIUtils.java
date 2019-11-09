package arena.ui;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;

import java.io.InputStream;

import static arena.logic.ArenaConstants.*;

public class ArenaUIUtils {
    public static ClipboardContent copyToClipboardText(String s) {
        final ClipboardContent content = new ClipboardContent();

        content.putString(s);
        return content;
    }

    public static ClipboardContent copyToClipboardImage(Label lbl) {
        WritableImage snapshot = lbl.snapshot(new SnapshotParameters(), null);
        final ClipboardContent content = new ClipboardContent();
        content.putImage(snapshot);
        return content;
    }

    public static ClipboardContent copyToClipboardImageFromFile(String path) {
        final ClipboardContent content = new ClipboardContent();
        content.putImage(ArenaUIUtils.getImage(path));
        return content;
    }

    public static Image getImage(String path) {
        InputStream is = ArenaUIUtils.class.getResourceAsStream(path);
        return new Image(is);
    }

    public static ImageView setIconFromPath(String path) {
        InputStream is = ArenaUIUtils.class.getResourceAsStream(path);
        ImageView iv = new ImageView(new Image(is));
        iv.setFitHeight(GRID_HEIGHT);
        iv.setFitWidth(GRID_WIDTH);
        return iv;
    }

    public static ImageView setIcon(Image i) {
        ImageView iv = new ImageView(i);
        iv.setFitHeight(GRID_HEIGHT);
        iv.setFitWidth(GRID_WIDTH);
        return iv;
    }

    public static ImageView setIcon(Image i, int height, int width) {
        ImageView iv = new ImageView(i);
        iv.setFitHeight(height);
        iv.setFitWidth(width);
        return iv;
    }
}
