package com.todoist.todoist.structures;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public abstract class BaseController<T extends BaseModel> {
    private final MongoCollection<Document> collection;
    private final String name;
    private HashMap<ObjectId, T> cache;

    public BaseController(MongoDatabase db, String name) {
        this.name = name.substring(0, name.length() - 1);
        collection = db.getCollection(name);
    }

    protected abstract T documentToModel(Document doc);
    protected abstract Document modelToDocument(T model);

    public T findById(ObjectId id) {
        if (cache != null && cache.get(id) != null)
            return cache.get(id);
        Document doc = collection.find(eq("_id", id)).first();
        if (doc == null)
            return null;
        return documentToModel(doc);
    }
    public HashMap<ObjectId, T> list() {
        HashMap<ObjectId, T> map = new HashMap<>();

        if (cache != null)
            return cache;

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            try {
                while (cursor.hasNext()) {
                    Document current = cursor.next();
                    T model = documentToModel(current);
                    map.put(model.id, model);
                }
            } finally {
                cursor.close();
            }
        }

        cache = map;
        return map;
    }
    public void insert(T item) {
        if (item == null) {
            System.err.println("Insert Error: Provided null " + name);
            return;
        }
        collection.insertOne(modelToDocument(item));
        if (cache != null)
            cache.put(item.id, item);
        System.out.println("Inserted " + name + ": " + item.id);
    }
    public void delete(T item) {
        if (item == null) {
            System.err.println("Delete Error: Provided null " + name);
            return;
        }
        delete(item.id);
    }
    public void delete(ObjectId id) {
        try {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            if (result.getDeletedCount() == 0)
                throw new Exception("No " + name +  " with id " + id);
            if (cache != null)
                cache.remove(id);
            System.out.println("Deleted " + name + ": " + id);
        } catch (Exception e) {
            System.err.println("Delete " + name + " Error: " + e);
        }
    }
    public void update(T item, Bson data) {
        if (item == null) {
            System.err.println("Update Error: Provided null " + name);
            return;
        }
        update(item.id, data);
    }
    public void update(ObjectId id, Bson data) {
        try {
            UpdateResult result = collection.updateOne(eq("_id", id), data);
            if (result.getModifiedCount() == 0)
                throw new Exception("No " + name + " with id " + id);
            if (cache != null)
                cache.put(id, cache.get(id));
            System.out.println("Updated project: " + id);
        } catch (Exception e) {
            System.err.println("Update " + name + " Error: " + e);
        }
    }
}
