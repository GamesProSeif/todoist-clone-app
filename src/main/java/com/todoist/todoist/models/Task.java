package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Task extends BaseModel {
    public String title;
    public String description;
    public LocalDate dueDate;
    public boolean checked;
    public Project project;
    public Section section;
    public ArrayList<ObjectId> tags;

    public Task() {
        this.id = new ObjectId();
        this.tags = new ArrayList<>();
    }

    public Task(
            String title,
            String description,
            LocalDate dueDate,
            boolean checked,
            Project project,
            Section section,
            ArrayList<ObjectId> tags
    ) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.checked = checked;
        this.project = project;
        this.section = section;
        this.tags = tags;
    }

    public Task(
            ObjectId id,
            String title,
            String description,
            LocalDate dueDate,
            boolean checked,
            Project project,
            Section section,
            ArrayList<ObjectId> tags
    ) {
        this(title, description, dueDate, checked, project, section, tags);
        this.id = id;
    }
}
