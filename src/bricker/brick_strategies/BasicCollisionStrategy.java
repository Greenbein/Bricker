package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;

    /**
     * constructor for Basic collision Strategy
     * @param brickerGameManager - game manager of bricker
     */
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * collision method: delete the bricks when we hit with a ball
     * @param one - first collision object
     * @param two - second collision object
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        brickerGameManager.removeObject(one, Layer.STATIC_OBJECTS);
        brickerGameManager.checkForGameEnd();
    }

}
