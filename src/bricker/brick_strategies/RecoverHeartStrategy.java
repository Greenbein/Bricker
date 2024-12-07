package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameObject;

public class RecoverHeartStrategy extends SpecialCollisionStrategy{
    public RecoverHeartStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy, brickerGameManager);
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().createFallingHeart(one.getCenter().x(), one.getCenter().y());
    }
}
