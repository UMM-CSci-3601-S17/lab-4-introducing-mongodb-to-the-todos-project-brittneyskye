import { Component, OnInit } from '@angular/core';
import { TodoListService } from "./todo-list.service";
import { Todo } from "./todo";

@Component({
    selector: 'todo-component',
    templateUrl: 'todo.component.html'
})
export class TodoComponent implements OnInit {
    public todo: Todo = null;
    private id: string;
    private owner: string;

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.gettodos();
    }

    private subscribeToService() {
        if (this.id) {
            this.todoListService.getTodoById(this.id).subscribe(
                todo => this.todo = todo,
                err => {
                    console.log(err);
                }
            );
        }

    }

    setId(id: string) {
        this.id = id;
        this.subscribeToService();
    }

    setOwner(owner: string ) {
        this.owner = owner;
        this.subscribeToService();
    }

    ngOnInit(): void {
        this.subscribeToService()
    }
}
