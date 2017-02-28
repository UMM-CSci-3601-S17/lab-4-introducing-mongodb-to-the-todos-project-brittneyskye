package umm3601;

import umm3601.user.UserController;
import umm3601.todos.TodoController;

import java.io.IOException;

import static spark.Spark.*;


public class Server {
    public static void main(String[] args) throws IOException {

        UserController userController = new UserController();
        TodoController todoController = new TodoController();

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
 
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // Simple example route
        get("/hello", (req, res) -> "Hello World");

        // Redirects for the "home" page
        redirect.get("", "/");
        redirect.get("/", "http://localhost:9000");

        // List users
        get("api/users", (req, res) -> {
            res.type("application/json");
            return userController.listUsers(req.queryMap().toMap());
        });

        //List Todos
        get("api/todos", (req, res) -> {
            res.type("application/json");
            return todoController.listTodos(req.queryMap().toMap());
        });

        // See specific user
        get("api/users/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return userController.getUser(id);
        });

        //See specific todo
        get("api/todos/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return todoController.getTodo(id);
        });

        // Get average ages by company
        get("api/avgUserAgeByCompany", (req, res) -> {
            res.type("application/json");
            return userController.getAverageAgeByCompany();
        });

        get("api/todoOwners", (req, res) -> {
            res.type("application/json");
            return todoController.getTodoOwners();
        });

        // Handle "404" file not found requests:
        notFound((req, res) -> {
            res.type("text");
            res.status(404);
            return "Sorry, we couldn't find that!";
        });

    }

}
