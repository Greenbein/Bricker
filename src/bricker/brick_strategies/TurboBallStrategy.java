package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class TurboBallStrategy extends SpecialCollisionStrategy{
    public TurboBallStrategy(CollisionStrategy collisionStrategy,BrickerGameManager brickerGameManager) {
        super(collisionStrategy,brickerGameManager);
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        //collisionStrategy().onCollision(one, two);
        if(two.getTag().equals("main_ball")){
            brickerGameManager().onTurbo();
        }
        collisionStrategy().onCollision(one, two);
        //brickerGameManager().removeObject(one, Layer.STATIC_OBJECTS);
    }
}
