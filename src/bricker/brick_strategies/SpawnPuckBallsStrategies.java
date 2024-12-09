package bricker.brick_strategies;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class SpawnPuckBallsStrategies extends SpecialCollisionStrategy{
    /**
     * Constructor for SpawnPuckBallsStrategies
     * @param collisionStrategy - first behaviour of SpawnPuckBallsStrategies.
     *                          BasicCollision is a default collision in this case
     *                          If we are in the case if double strategy, collisionStrategy
     *                          can be equals to all others SpecialCollisions
     *
     * @param brickerGameManager  GameManager class Bricker
     */
    public SpawnPuckBallsStrategies(CollisionStrategy collisionStrategy,BrickerGameManager brickerGameManager){
       super(collisionStrategy,brickerGameManager);
    }
    /**
     * After the collision we have to spawn two puck balls
     * @param one the first object in collision
     * @param two the second object in collision
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().spawnPuckBalls(one.getCenter().x(),one.getCenter().y());
    }
}
