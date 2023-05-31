package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

public class Project extends BaseModel {
    public String title;

    public Project() {
        id = new ObjectId();
    }

    public Project(String title) {
        this();
        this.title = title;
    }

    public Project(ObjectId id, String title) {
        this(title);
        this.id = id;
    }
}
