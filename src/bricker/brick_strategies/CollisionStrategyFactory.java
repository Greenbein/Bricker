package bricker.brick_strategies;

import bricker.BrickerGameManager;

import java.util.Random;


public class CollisionStrategyFactory {
    private final BrickerGameManager brickerGameManager;
    private static final int SPAWN_PUCK_BALLS = 0;
    private static final int ADD_PADDLE = 1;
    private static final int TURBO = 2;
    private static final int RECOVER = 3;
    private static final int DOUBLE = 4;

    private final Random rand;

    public CollisionStrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.rand = new Random();
    }

    public CollisionStrategy getCollisionStrategy() {
        CollisionStrategy basic = new BasicCollisionStrategy(brickerGameManager);
        int isBasic = rand.nextInt(2);
        if (isBasic == 0) {
            return basic;
        }
        return buildStrategyFactory(basic, 0, 1);
    }

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
            default:
                break;
        }
        return collisionStrategy;
    }
}
