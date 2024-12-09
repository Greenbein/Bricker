package bricker.brick_strategies;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class RecoverHeartStrategy extends SpecialCollisionStrategy{
    /**
     * Constructor for RecoverHeartStrategy
     * @param collisionStrategy - first behaviour of RecoverHeartStrategy.
     *                          BasicCollision is a default collision in this case
     *                          If we are in the case if double strategy, collisionStrategy
     *                          can be equals to all others SpecialCollisions
     *
     * @param brickerGameManager  GameManager class for bricker
     */
    public RecoverHeartStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy, brickerGameManager);
    }

    /**
     * Create one falling heart after we hit the brick is hit with a ball. If we catch it with our paddle
     * it will increase the hearts amount
     * @param one - first collision object
     * @param two - second collision object
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().createFallingHeart(one.getCenter().x(), one.getCenter().y());
    }
}
