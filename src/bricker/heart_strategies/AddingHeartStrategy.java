package bricker.heart_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

public class AddingHeartStrategy implements HeartCollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    public AddingHeartStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        if(two.shouldCollideWith(one)){
            brickerGameManager.increaseAmountOfHearts();
            brickerGameManager.removeObject(one,Layer.DEFAULT);
        }
        brickerGameManager.removeObject(one, Layer.STATIC_OBJECTS);
    }


}
