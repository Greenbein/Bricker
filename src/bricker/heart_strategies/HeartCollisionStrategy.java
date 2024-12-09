package bricker.heart_strategies;

import danogl.GameObject;

/**
 * interface of each heart object
 */
public interface HeartCollisionStrategy {
    void onCollision(GameObject one, GameObject two);
}
