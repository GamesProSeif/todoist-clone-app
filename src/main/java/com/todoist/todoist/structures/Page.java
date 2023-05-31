package com.todoist.todoist.structures;

import javafx.scene.layout.Pane;

public abstract class Page {
    protected TodoistApp app;

    public Page(TodoistApp app) {
        this.app = app;
    }

    public abstract Pane getContent();
}
