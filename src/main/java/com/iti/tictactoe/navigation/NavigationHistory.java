package com.iti.tictactoe.navigation;

import javafx.scene.Scene;

import java.util.Stack;

public class NavigationHistory {
    private final Stack<Scene> history = new Stack<>();

    // go to the next
    public void pushScene(Scene scene) {
        history.push(scene);
    }

    // remove and return to the last one in the top
    public void popScene() {
        if (!history.isEmpty()) {
            history.pop();
        }
    }

    // return the top scene without removing it
    public Scene peekScene() {
        if (history.isEmpty()) {
            return null;
        }
        return history.peek();
    }

    // Check if history is empty
    public boolean isEmpty() {
        return history.isEmpty();
    }

    public int size() {
        return history.size();
    }
}
