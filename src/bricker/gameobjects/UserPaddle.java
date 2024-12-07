package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import bricker.ManagerConstants;

import static bricker.ManagerConstants.WALL_WIDTH;

public class UserPaddle extends GameObject {

    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final float screenWidth;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if(this.getTopLeftCorner().x()>0+WALL_WIDTH){
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if(this.getTopLeftCorner().x()+this.getDimensions().x()<this.screenWidth-WALL_WIDTH){
                movementDir = movementDir.add(Vector2.RIGHT);
            }
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
     * @param inputListener
     */
    public UserPaddle(Vector2 topLeftCorner,
                      Vector2 dimensions,
                      Renderable renderable,
                      UserInputListener inputListener,
                      float screenWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.screenWidth = screenWidth;
    }
}
