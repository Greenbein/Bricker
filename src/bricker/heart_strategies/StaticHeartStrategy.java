package bricker.heart_strategies;

import danogl.GameObject;

public class StaticHeartStrategy implements HeartCollisionStrategy {
    /**
     * constructor of static hearts
     */
    public StaticHeartStrategy() {}

    /**
     * implements onCollision behavior of static heart
     * @param one static heart
     * @param two other object
     */
    @Override
    public void onCollision(GameObject one, GameObject two) {}
}
