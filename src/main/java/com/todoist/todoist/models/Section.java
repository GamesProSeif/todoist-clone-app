package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

public class Section extends BaseModel {
    public String title;
    public Project project;

    public Section() {
        this.id = new ObjectId();
    }

    public Section(String title, Project project) {
        this();
        this.title = title;
        this.project = project;
    }

    public Section(ObjectId id, String title, Project project) {
        this(title, project);
        this.id = id;
    }
}
