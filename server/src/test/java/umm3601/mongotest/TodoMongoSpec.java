package umm3601.mongotest;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TodoMongoSpec {

    private MongoCollection<Document> todoDocuments;

    @Before
    public void clearAndPopulateDB() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("testingdb");
        todoDocuments = db.getCollection("todos");
        todoDocuments.drop();
        List<Document> testUsers = new ArrayList<>();
        testUsers.add(Document.parse("{\n" +
                "                    id: \"Chris_Id\",\n" +
                "                    owner: \"Chris\",\n" +
                "                    status: \"false\",\n" +
                "                    category: \"software\"\n" +
                "                    body: \"chris likes to make things with computers.\"\n" +
                "                }"));

        testUsers.add(Document.parse("{\n" +
                "                    id: \"Brittney_Id\",\n" +
                "                    owner: \"Brittney\",\n" +
                "                    status: \"false\",\n" +
                "                    category: \"software\"\n" +
                "                    body: \"brittney likes to make things with computers.\"\n" +
                "                }"));

        testUsers.add(Document.parse("{\n" +
                "                    id: \"Chris2_Id\",\n" +
                "                    owner: \"Chris\",\n" +
                "                    status: \"false\",\n" +
                "                    category: \"software\"\n" +
                "                    body: \"chris2 likes to make things with computers.\"\n" +
                "                }"));

        testUsers.add(Document.parse("{\n" +
                "                    id: \"Skye_Id\",\n" +
                "                    owner: \"Skye\",\n" +
                "                    status: \"false\",\n" +
                "                    category: \"software\"\n" +
                "                    body: \"skye likes to make things with computers.\"\n" +
                "                }"));

        testUsers.add(Document.parse("{\n" +
                "                    id: \"Nic_Id\",\n" +
                "                    owner: \"Nic\",\n" +
                "                    status: \"false\",\n" +
                "                    category: \"software\"\n" +
                "                    body: \"Nic likes to make things with computers.\"\n" +
                "                }"));


        todoDocuments.insertMany(testUsers);
    }

    private List<Document> intoList(MongoIterable<Document> documents) {
        List<Document> users = new ArrayList<>();
        documents.into(users);
        return users;
    }

    @Test
    public void testGetUniqueTodoOwners(){

        AggregateIterable<Document> documents
                = todoDocuments.aggregate(
                Arrays.asList(
                        Aggregates.group("$owner"),
                        Aggregates.sort(Sorts.ascending("_id"))
                ));

        List<Document> docs = intoList(documents);

        assertEquals(docs.size(), 4);
        assertEquals("Brittney", docs.get(0).get("_id"));
        assertEquals("Chris", docs.get(1).get("_id"));
        assertEquals("Nic", docs.get(2).get("_id"));
        assertEquals("Skye", docs.get(3).get("_id"));

    }
}
