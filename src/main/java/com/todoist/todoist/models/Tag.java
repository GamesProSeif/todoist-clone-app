package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

public class Tag extends BaseModel {
    public String title;
    public int color;
    public Project project;

    public Tag() {
        this.id = new ObjectId();
    }

    public Tag(String title, int color, Project project) {
        this();
        this.title = title;
        this.color = color;
        this.project = project;
    }

    public Tag(ObjectId id, String title, int color, Project project) {
        this(title, color, project);
        this.id = id;
    }
}
