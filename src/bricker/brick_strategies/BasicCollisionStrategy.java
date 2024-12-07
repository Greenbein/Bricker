package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class BasicCollisionStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        brickerGameManager.removeObject(one, Layer.STATIC_OBJECTS);
        brickerGameManager.checkForGameEnd();
    }

}
