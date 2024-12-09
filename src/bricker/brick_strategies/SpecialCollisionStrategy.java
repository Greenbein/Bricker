package bricker.brick_strategies;
import bricker.main.BrickerGameManager;

public abstract class SpecialCollisionStrategy implements CollisionStrategy {
    private final CollisionStrategy collisionStrategy;
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructor for abstract class SpecialCollisionStrategy
     * It will contain CollisionStrategy and BrickerGameManager
     * @param collisionStrategy - all class that extends the abstract class will use it
     *                          in order to implement their own collision method
     * @param brickerGameManager - all class that extends the abstract class will use it
     *                          in order to implement their own collision method
     */

    /**
     * Constructor for SpecialCollisionStrategy
     * Only in classes that extend from this abstract class we are able two create SpecialCollisionStrategy object
     * @param collisionStrategy - first collision object
     * @param brickerGameManager - game manager for bricker
     */
    protected SpecialCollisionStrategy(CollisionStrategy collisionStrategy, BrickerGameManager brickerGameManager) {
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
