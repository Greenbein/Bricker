package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;


public class TurboBallStrategy extends SpecialCollisionStrategy{
    /**
     * Constructor for TurboBallStrategy
     * @param collisionStrategy - first behaviour of TurboBallStrategy.
     *                          BasicCollision is a default collision in this case
     *                          If we are in the case if double strategy, collisionStrategy
     *                          can be equals to all others SpecialCollisions
     *
     * @param brickerGameManager - GameManager class Bricker
     */
    public TurboBallStrategy(CollisionStrategy collisionStrategy,BrickerGameManager brickerGameManager) {
        super(collisionStrategy,brickerGameManager);
    }

    /**
     * After the collision we have ti turn on turbo mode for player ball
     * @param one the first object in collision
     * @param two the second object in collision
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        if(two.getTag().equals("main_ball")){
            brickerGameManager().onTurbo();
        }
        collisionStrategy().onCollision(one, two);
    }
}
