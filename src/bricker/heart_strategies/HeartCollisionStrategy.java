package bricker.heart_strategies;

import danogl.GameObject;

public interface HeartCollisionStrategy {
    void onCollision(GameObject one, GameObject two);
}
