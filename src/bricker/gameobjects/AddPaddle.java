package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class AddPaddle extends Paddle {
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
     * @param inputListener - we use it in order to implement update that is in Paddle class
     * @param screenWidth  - width of a game screen
     */
    public AddPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                     Renderable renderable, UserInputListener inputListener,
                     float screenWidth, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                screenWidth);
        this.brickerGameManager = brickerGameManager;
        this.collisionCounter = 0;
    }

    /**
     * After three collision the game manager should remove the extra paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
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
