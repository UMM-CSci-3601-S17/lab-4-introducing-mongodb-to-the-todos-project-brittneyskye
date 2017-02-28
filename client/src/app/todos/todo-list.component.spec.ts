// import { ComponentFixture, TestBed, async } from "@angular/core/testing";
// import { Todo } from "./todo";
// import { TodoListComponent } from "./todo-list.component";
// import { TodoListService } from "./todo-list.service";
// import { Observable } from "rxjs";
// import { PipeModule } from "../../pipe.module";
//
// describe("Todo list", () => {
//
//     let todoList: TodoListComponent;
//     let fixture: ComponentFixture<TodoListComponent>;
//
//     let todoListServiceStub: {
//         getTodos: () => Observable<Todo[]>
//     };
//
//     beforeEach(() => {
//         // stub TodoService for test purposes
//         todoListServiceStub = {
//             getTodos: () => Observable.of([
//                 {
//                     id: "chris_id",
//                     owner: "Chris",
//                     status: true,
//                     category: "UMM",
//                     body: "this is the email for chris@this.that"
//                 },
//                 {
//                     id: "skye_id",
//                     owner: "Skye",
//                     status: false,
//                     category: "UMM",
//                     body: "this is the email for skye@this.that"
//                 },
//                 {
//                     id: "brittney_id",
//                     owner: "Brittney",
//                     status: true,
//                     category: "UMM",
//                     body: "this is the email for brittney@this.that"
//                 },
//                 {
//                     id: "chris2_id",
//                     owner: "Chris",
//                     status: false,
//                     category: "UMM",
//                     body: "this is the email for chris2@this.that"
//                 }
//                 ])
//         };
//
//         TestBed.configureTestingModule({
//             imports: [PipeModule],
//             declarations: [ TodoListComponent ],
//             // providers:    [ TodoListService ]  // NO! Don't provide the real service!
//             // Provide a test-double instead
//             providers:    [ { provide: TodoListService, useValue: todoListServiceStub } ]
//         })
//     });
//
//     beforeEach(async(() => {
//         TestBed.compileComponents().then(() => {
//             fixture = TestBed.createComponent(TodoListComponent);
//             todoList = fixture.componentInstance;
//             fixture.detectChanges();
//         });
//     }));
//
//     it("contains all unique owners", () => {
//         expect(todoList.todos.length).toBe(3);
//     });
//
//     it("contains all the todos", () => {
//         expect(todoList.todos.length).toBe(3);
//     });
//
//     it("contains a todo named 'Chris'", () => {
//         expect(todoList.todos.some((todo: Todo) => todo.name === "Chris" )).toBe(true);
//     });
//
//     it("contain a todo named 'Jamie'", () => {
//         expect(todoList.todos.some((todo: Todo) => todo.name === "Jamie" )).toBe(true);
//     });
//
//     it("doesn't contain a todo named 'Santa'", () => {
//         expect(todoList.todos.some((todo: Todo) => todo.name === "Santa" )).toBe(false);
//     });
//
//     it("has two todos that are 37 years old", () => {
//         expect(todoList.todos.filter((todo: Todo) => todo.age === 37).length).toBe(2);
//     });
//
// });
