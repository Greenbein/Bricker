package bricker.gameobjects;

import bricker.heart_strategies.HeartCollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Heart extends GameObject {

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    private final GameObject heartShouldCollideWith;
    private final HeartCollisionStrategy heartCollisionStrategy;
    public Heart(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 HeartCollisionStrategy heartCollisionStrategy,
                 GameObject heartShouldCollideWith) {
        super(topLeftCorner, dimensions, renderable);
        this.heartCollisionStrategy = heartCollisionStrategy;
        this.heartShouldCollideWith = heartShouldCollideWith;
    }

    /**
     * Use heart collision strategy in case of collision
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        this.heartCollisionStrategy.onCollision(this, other);
    }

    /**
     * Check what objects can be in collision with the heart
     * We do it by tag verifying
     * @param other The other GameObject.
     * @return
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(this.heartShouldCollideWith.getTag());
    }
}
