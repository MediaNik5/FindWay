package org.medianik.findway.gameobject;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface KeyHandler{

    boolean isKeyToHandle(KeyCode key);

    void handleKey(KeyEvent key, int tick);
}
