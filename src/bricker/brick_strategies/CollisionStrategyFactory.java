package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;


public class CollisionStrategyFactory {
    private final BrickerGameManager brickerGameManager;
    private static final int SPAWN_PUCK_BALLS = 0;
    private static final int ADD_PADDLE = 1;
    private static final int TURBO = 2;
    private static final int RECOVER = 3;
    private static final int DOUBLE = 4;

    private final Random rand;

    /**
     * Constructor for CollisionStrategyFactory
     * @param brickerGameManager - game manager of bricker
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.rand = new Random();
    }

    /**
     * At first, the method chooses a random number from [0,1]
     * 50% probability to get BasicCollision and finish
     * 50% probability to get SpecialCollision and pass to buildStrategyFactory method
     * @return Collision of the object
     */
    public CollisionStrategy getCollisionStrategy() {
        CollisionStrategy basic = new BasicCollisionStrategy(brickerGameManager);
        int isBasic = rand.nextInt(2);
        if (isBasic == 0) {
            return basic;
        }
        return buildStrategyFactory(basic, 0, 1);
    }

    //here we use factory in order to build our Special Collision
    //if we get a number [0,1,2,3], then we choose one of 4 regular SpecialCollision types and finish
    //else we build our collision in a recursive way (DOUBLE)
    //there can be only 3 different Special Collisions. It occurs only when we choose DOUBLE two times
    private CollisionStrategy buildStrategyFactory(CollisionStrategy collisionStrategy,
                                                   int counterDouble,
                                                   int counterStrategies) {
        if (counterStrategies == 0) {
            return collisionStrategy;
        }
        int strategyType = rand.nextInt(5);
        switch (strategyType) {
            case SPAWN_PUCK_BALLS:
                return buildStrategyFactory(
                        new SpawnPuckBallsStrategies(collisionStrategy, brickerGameManager),
                        counterDouble, counterStrategies - 1);
            case ADD_PADDLE:
                return buildStrategyFactory(
                        new AddPaddleStrategy(collisionStrategy, brickerGameManager),
                        counterDouble, counterStrategies - 1);
            case TURBO:
                return buildStrategyFactory(
                        new TurboBallStrategy(collisionStrategy, brickerGameManager),
                        counterDouble, counterStrategies - 1);
            case RECOVER:
                return buildStrategyFactory(
                        new RecoverHeartStrategy(collisionStrategy, brickerGameManager),
                        counterDouble, counterStrategies - 1);
            case DOUBLE:
                if (counterDouble < 2) {
                    return buildStrategyFactory(collisionStrategy, counterDouble + 1, counterStrategies + 1);
                }
                break;
        }
        return buildStrategyFactory(collisionStrategy,counterDouble, counterStrategies);
    }
}
