import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { Message } from "src/app/model/myhouse";

@Component({
    selector: 'app-messages-table',
    templateUrl: './messages-table.component.html',
    styleUrls: [],
    standalone: true,
    imports: [CommonModule, FontAwesomeModule]
  })
  
  
  export class MessagesTableComponent {
    @Input() messages!: Message[];
    @Input() page!: number;
    @Input() pageInfo!: any;
    @Output() paginationEmmiter = new EventEmitter<string>();
    faChevronLeft: IconDefinition = faChevronLeft;
    faChevronRight: IconDefinition = faChevronRight;

    get columnNames(): string[] {
        return Object.getOwnPropertyNames(new Message());
      }

    prevPage = () => {
        this.paginationEmmiter.emit("prevPage");
    }

    nextPage = () => {
        this.paginationEmmiter.emit("nextPage");
    }
  }