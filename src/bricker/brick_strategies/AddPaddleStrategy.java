package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;


public class AddPaddleStrategy extends SpecialCollisionStrategy{
    /**
     * Constructor for AddPaddleStrategy
     * @param collisionStrategy - first behaviour of AddPaddleStrategy.
     *      *                     BasicCollision is a default collision in this case
     *      *                     If we are in the case if double strategy, collisionStrategy
     *      *                     can be equals to all others SpecialCollisions
     * @param brickerGameManager - game manager of bricker
     */
    public AddPaddleStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        super(collisionStrategy,brickerGameManager);
    }

    /**
     * Create extra paddle after we hit the brick with the ball
     * @param one - first collision object
     * @param two - second collision object
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().createExtraPaddle();
    }
}
