package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;


public class AddPaddleStrategy extends SpecialCollisionStrategy{
    public AddPaddleStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy,brickerGameManager);
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().createExtraPaddle();
    }
}
