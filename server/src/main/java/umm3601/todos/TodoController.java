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

    public String getSummaryInformation(){

        String percentComplete = getPercentCompleteSubJson();
        String categorySubJson = getCategorySubJson();
        String ownerSubJson = getOwnerSubJson();


        String completeJson = "{" + percentComplete + ", " + categorySubJson + ", " + ownerSubJson + "}";
        return completeJson;
    }



    private String getPercentCompleteSubJson(){

        int totalTodos = (int) todoCollection.count(),
            totalCompleteTodos;

        AggregateIterable<Document> totalNumber = todoCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("status", true)),
                        Aggregates.group("$status", Accumulators.sum("numOfTodos", 1))
                )
        );

        String numberOfTodosJSON = JSON.serialize(totalNumber);

        int numberOfCompleteTodos = pullData(numberOfTodosJSON);

        float percentage = (float) numberOfCompleteTodos / (float) totalTodos;

        return createPercentCompleteSubJson(percentage);
    }

    private String createPercentCompleteSubJson(float percentage){
        return "\"percentToDosComplete\": " + percentage;
    }

    private String getCategorySubJson(){

        String[] categories = {"software design", "video games", "homework", "groceries"};
        int[] categoryValues = new int[8];

        for(int i = 0, j = 0; i < categories.length; i++, j+=2) {

            AggregateIterable<Document> totalNumberOfCategory = todoCollection.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.eq("category", categories[i])),
                            Aggregates.group("$category", Accumulators.sum("numOfCategory", 1))
                    )
            );

            AggregateIterable<Document> totalNumberOfCompleteCategory = todoCollection.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.eq("category", categories[i])),
                            Aggregates.match(Filters.eq("status", true)),
                            Aggregates.group("$category", Accumulators.sum("numOfCategory", 1))
                    )
            );

            String numberOfCategoryJSON = JSON.serialize(totalNumberOfCategory),
                    numberOfCompleteCategoryJSON = JSON.serialize(totalNumberOfCompleteCategory);

            int numberOfCategory = pullData(numberOfCategoryJSON),
                    numberOfCompleteCategory = pullData(numberOfCompleteCategoryJSON);

            categoryValues[j] = numberOfCategory;
            categoryValues[j+1] = numberOfCompleteCategory;
        }

        return createCategorySubJson(categories, categoryValues);
    }

    private String createCategorySubJson(String[] keys, int[] values){

        float[] percentages = new float[4];
        for(int i = 0, j = 0; i < 4; i++, j +=2)
            percentages[i] = (float) values[j+1] / (float) values[j];

        String json = "\"categoriesPercentComplete\": {";
        for(int i = 0; i < keys.length; i++){
            json += (" \"" + keys[i] + "\": " + percentages[i] + (i == keys.length - 1 ? "" : ","));
        }
        json += " }";

        return json;
    }

    private String getOwnerSubJson(){
        String[] owners = {"Barry", "Blanche", "Dawn", "Fry", "Roberta", "Workman" };
        int[] ownerValues = new int[12];

        for(int i = 0, j = 0; i < owners.length; i++, j+=2) {

            AggregateIterable<Document> totalNumberOfOwner = todoCollection.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.eq("owner", owners[i])),
                            Aggregates.group("$owner", Accumulators.sum("numOfOwner", 1))
                    )
            );

            AggregateIterable<Document> totalNumberOfCompleteOwner = todoCollection.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.eq("owner", owners[i])),
                            Aggregates.match(Filters.eq("status", true)),
                            Aggregates.group("$owner", Accumulators.sum("numOfOwner", 1))
                    )
            );

            String numberOfOwnerJSON = JSON.serialize(totalNumberOfOwner),
                    numberOfOwnerCategoryJSON = JSON.serialize(totalNumberOfCompleteOwner);

            int numberOfOwner = pullData(numberOfOwnerJSON),
                    numberOfCompleteOwner = pullData(numberOfOwnerCategoryJSON);

            ownerValues[j] = numberOfOwner;
            ownerValues[j+1] = numberOfCompleteOwner;
        }

        return createOwnersSubJson(owners, ownerValues);
    }

    private String createOwnersSubJson(String[] keys, int[] values){
        float[] percentages = new float[6];
        for(int i = 0, j = 0; i < 6; i++, j +=2)
            percentages[i] = (float) values[j+1] / (float) values[j];

        String json = "\"ownersPercentComplete\": {";
        for(int i = 0; i < keys.length; i++){
            json += (" \"" + keys[i] + "\": " + percentages[i] + (i == keys.length - 1 ? "" : ","));
        }
        json += " }";

        return json;
    }

    private int pullData(String json){

        int result = 0;

        String[] split = json.split(":");
        String target = split[2];
        split = split[2].split("}");
        target = split[0].trim();

        result = Integer.parseInt(target);

        return result;
    }
}
