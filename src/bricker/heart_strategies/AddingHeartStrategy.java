package bricker.heart_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class AddingHeartStrategy implements HeartCollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    /**
     * constructor of the strategy of adding hearts
     * @param brickerGameManager field that contain an object
     *                           of class brickerGameManager
     */
    public AddingHeartStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * implements onCollision behavior of falling heart
     * @param one falling heart
     * @param two other object
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        if(two.shouldCollideWith(one)){
            brickerGameManager.increaseAmountOfHearts();
            brickerGameManager.removeObject(one,Layer.DEFAULT);
        }
        brickerGameManager.removeObject(one, Layer.STATIC_OBJECTS);
    }


}
