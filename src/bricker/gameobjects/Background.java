package bricker.gameobjects;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Background extends GameObject {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Background(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }
}


