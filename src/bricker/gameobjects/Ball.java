package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import static bricker.ManagerConstants.*;

public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;
    private BrickerGameManager brickerGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        //if(other.getTag().equals("brick")){
            collisionSound.play();
        //}
        if(brickerGameManager.isTurbo){
            System.out.println(collisionCounter);
        }
        //collisionSound.play();
        this.collisionCounter++;
    }
    public void setCollisionCounter(int collisionCounter) {
        this.collisionCounter = collisionCounter;
    }
    public int getCollisionCounter() {
        return this.collisionCounter;
    }
}