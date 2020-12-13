package logic;

import javafx.scene.Node;

public interface ShakeItem {
    default void shakeItem(Node node){
        Shake shake = new Shake(node);
        shake.playAnim();
    }
}
