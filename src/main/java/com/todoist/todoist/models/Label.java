package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

public class Label extends BaseModel {
    public String title;
    public int color;
    public Project project;

    public Label() {
        this.id = new ObjectId();
    }

    public Label(String title, int color, Project project) {
        this();
        this.title = title;
        this.color = color;
        this.project = project;
    }

    public Label(ObjectId id, String title, int color, Project project) {
        this(title, color, project);
        this.id = id;
    }
}
