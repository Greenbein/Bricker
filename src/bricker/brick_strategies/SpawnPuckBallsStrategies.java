package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameObject;

public class SpawnPuckBallsStrategies extends SpecialCollisionStrategy{
    public SpawnPuckBallsStrategies(CollisionStrategy collisionStrategy,BrickerGameManager brickerGameManager){
       super(collisionStrategy,brickerGameManager);
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {
        collisionStrategy().onCollision(one, two);
        brickerGameManager().spawnPuckBalls(one.getCenter().x(),one.getCenter().y());
    }
}
