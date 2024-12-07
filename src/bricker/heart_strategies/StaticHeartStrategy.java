package bricker.heart_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class StaticHeartStrategy implements HeartCollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    public StaticHeartStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject one, GameObject two) {}
}
