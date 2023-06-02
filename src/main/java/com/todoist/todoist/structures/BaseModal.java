package com.todoist.todoist.structures;

import javafx.stage.Stage;

public abstract class BaseModal {
    protected final TodoistApp app;

    public BaseModal(TodoistApp app) {
        this.app = app;
    }

    abstract public Stage getContent();
}
