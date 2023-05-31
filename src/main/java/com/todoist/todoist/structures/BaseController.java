package com.todoist.todoist.controllers;

import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.HashMap;

public interface BaseController<T> {
    T findById(ObjectId id);
    HashMap<ObjectId, T> list();
    void insert(T item);
    void delete(T item);
    void delete(ObjectId id);
    void update(T item, Bson options);
    void update(ObjectId id, Bson data);
}
