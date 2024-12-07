package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class AddPaddle extends UserPaddle {
    private final BrickerGameManager brickerGameManager;
    private int collisionCounter;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener
     * @param screenWidth
     * @param screenWidth
     */
    public AddPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                     Renderable renderable, UserInputListener inputListener,
                     float screenWidth, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                screenWidth);
        this.brickerGameManager = brickerGameManager;
        this.collisionCounter = 0;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(this.collisionCounter==3){
            this.brickerGameManager.removeObject(this, Layer.DEFAULT);
            this.brickerGameManager.setExtraPaddleExists(false);
        }
        else{
            this.collisionCounter++;
        }
    }
}
