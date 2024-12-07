package bricker.brick_strategies;
import bricker.BrickerGameManager;

public abstract class SpecialCollisionStrategy implements CollisionStrategy {
    private final CollisionStrategy collisionStrategy;
    private final BrickerGameManager brickerGameManager;

    public SpecialCollisionStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
        this.collisionStrategy = collisionStrategy;
        this.brickerGameManager = brickerGameManager;
    }
    public CollisionStrategy collisionStrategy() {
        return collisionStrategy;
    }
    public BrickerGameManager brickerGameManager() {
        return brickerGameManager;
    }


}
