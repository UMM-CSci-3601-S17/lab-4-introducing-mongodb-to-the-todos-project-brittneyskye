package umm3601.todos;

/**
 * Created by ferri082 on 2/21/17.
 */

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;


public class TodoController {

    private final MongoCollection<Document> todoCollection;

    public TodoController() throws IOException {
        // Set up our server address
        // (Default host: 'localhost', default port: 27017)
        // ServerAddress testAddress = new ServerAddress();

        // Try connecting to the server
        //MongoClient mongoClient = new MongoClient(testAddress, credentials);
        MongoClient mongoClient = new MongoClient(); // Defaults!

        // Try connecting to a database
        MongoDatabase db = mongoClient.getDatabase("test");

        todoCollection = db.getCollection("todos");

    }

    // List todos
    public String listTodos(Map<String, String[]> queryParams) {

        List<Bson> aggregateParams = new ArrayList<>();

        if (queryParams.containsKey("owner")) {
            String owner = queryParams.get("owner")[0];
            aggregateParams.add(Aggregates.match(Filters.eq("owner", owner)));
        }

        if (queryParams.containsKey("status")) {
            String status = queryParams.get("status")[0];
            aggregateParams.add(Aggregates.match(Filters.eq("status", Boolean.parseBoolean(status))));
        }

        if (queryParams.containsKey("body")) {
            String body = queryParams.get("body")[0];
            aggregateParams.add(Aggregates.match(Filters.regex("body", body)));
        }

        AggregateIterable<Document> matchingTodos = todoCollection.aggregate(aggregateParams);

        return JSON.serialize(matchingTodos);
    }

    // Get a single todo
    public String getTodo(String id) {
        FindIterable<Document> jsonTodos
                = todoCollection
                .find(eq("_id", new ObjectId(id)));

        Iterator<Document> iterator = jsonTodos.iterator();

        Document user = iterator.next();

        return user.toJson();
    }

    public String getTodoOwners(){
        AggregateIterable<Document> documents
                = todoCollection.aggregate(
                Arrays.asList(
                        Aggregates.group("$owner"),
                        Aggregates.sort(Sorts.ascending("_id"))
                ));
        System.err.println(JSON.serialize(documents));
        return JSON.serialize(documents);
    }

}
