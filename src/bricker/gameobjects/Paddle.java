package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import static bricker.main.ManagerConstants.WALL_WIDTH;

public class Paddle extends GameObject {

    private static final float MOVEMENT_SPEED = 500;
    private final UserInputListener inputListener;
    private final float screenWidth;

    /**
     * implement paddle behavior and movement according to user moves
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.getTopLeftCorner().x()>0+WALL_WIDTH){
                movementDir = movementDir.add(Vector2.LEFT);
            }

        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && this.getTopLeftCorner().x()+this.getDimensions().x()<this.screenWidth-WALL_WIDTH){
                movementDir = movementDir.add(Vector2.RIGHT);
            }

        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener,
                  float screenWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.screenWidth = screenWidth;
    }
}
