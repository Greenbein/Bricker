package bricker.brick_strategies;

import danogl.GameObject;

public interface CollisionStrategy {
    /**
     * interface for BasicCollisionStrategy, i.e. there will be always onCollision method
     * @param one - first collision object
     * @param two - second collision object
     */
    void onCollision(GameObject one, GameObject two);
}
