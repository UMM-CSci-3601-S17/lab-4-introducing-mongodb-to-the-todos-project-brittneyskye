import { Component, OnInit } from '@angular/core';
import { TodoListService } from "./todo-list.service";
import { Todo } from "./todo";
import { FilterBy } from "./filter.pipe";


@Component({
    selector: 'todo-list-component',
    templateUrl: 'todo-list.component.html',
    providers: [ FilterBy ]
})

export class TodoListComponent implements OnInit {
    public todos: Todo[];
    public owners: Todo[];

    constructor(private todoListService: TodoListService) {
        // this.todos = this.todoListService.getTodos();
    }

    public getSelectedOwner(): string{
        return (<HTMLInputElement>document.getElementById("ownerDropdown")).value;
    }

    public getSelectedStatus(): string{

        var status;

        if((<HTMLInputElement>document.getElementById("statusBoth")).checked)
            status = (<HTMLInputElement>document.getElementById("statusBoth")).value

        else if((<HTMLInputElement>document.getElementById("statusComplete")).checked)
            status = (<HTMLInputElement>document.getElementById("statusComplete")).value

        else if((<HTMLInputElement>document.getElementById("statusIncomplete")).checked)
            status = (<HTMLInputElement>document.getElementById("statusIncomplete")).value

        return status;
    }

    public getSelectedBody(): string{
        return (<HTMLInputElement>document.getElementById("bodyText")).value;
    }


    public checkDatabaseFilters(): void{

        var owner = this.getSelectedOwner();
        var status = this.getSelectedStatus();
        var body = this.getSelectedBody();

        var filterUrl = "todos?";

        if(owner)
            filterUrl += "owner=" + owner + "&";
        if(status)
            filterUrl += "status=" + status + "&";
        if(body)
            filterUrl += "body=" + body + "&"

        console.log("(" + filterUrl + ")");

        this.todoListService.getTodosByFilter(filterUrl).subscribe(
            todos => this.todos = todos,
            err => {
                console.log(err);
            }
        );

    }

    ngOnInit(): void {

        this.todoListService.getTodos().subscribe(
            todos => this.todos = todos,
            err => {
                console.log(err);
            }
        );

        this.todoListService.getTodoOwners().subscribe(
            owners => this.owners = owners,
            err => {
                console.log(err);
            }
        );
    }
}
