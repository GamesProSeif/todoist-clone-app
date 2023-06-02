package com.todoist.todoist.models;

import com.todoist.todoist.structures.BaseModel;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Task extends BaseModel {
    public String title;
    public String description;
    public Date dueDate;
    public boolean checked;
    public String attachment;
    public Project project;
    public Section section;
    public ArrayList<Tag> tags;

    public Task() {
        this.id = new ObjectId();
        this.tags = new ArrayList<>();
    }

    public Task(
            String title,
            String description,
            Date dueDate,
            boolean checked,
            String attachment,
            Project project,
            Section section,
            ArrayList<Tag> tags
    ) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.checked = checked;
        this.attachment = attachment;
        this.project = project;
        this.section = section;
        this.tags = tags;
    }

    public Task(
            ObjectId id,
            String title,
            String description,
            Date dueDate,
            boolean checked,
            String attachment,
            Project project,
            Section section,
            ArrayList<Tag> tags
    ) {
        this(title, description, dueDate, checked, attachment, project, section, tags);
        this.id = id;
    }
}
