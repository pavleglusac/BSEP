import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faX } from "@fortawesome/free-solid-svg-icons";

interface FilterOption {
    filterField: string;
    messageType: string;
    messageText: string;
    messageValue: {from: number, to: number},
    messageTimestamp: {from: any, to: any}
}

export interface Filter {
    field: string;
    value: string;
}

@Component({
    selector: 'app-filter',
    templateUrl: './filter.component.html',
    styleUrls: ['./filter.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule, FontAwesomeModule]
  })
  
  
  export class FilterComponent implements OnInit {
      
      @Input() filter!: Filter[]
      @Output() applyFilter = new EventEmitter<Filter[]>()
      filterOptions: FilterOption[] = []
      faRemove: IconDefinition = faX;
      
    ngOnInit(): void {
        for (let f of this.filter) {
            let filterOpt: FilterOption = {filterField: '', messageType: '', messageText: '', messageValue: {from: 0, to: 0}, messageTimestamp: {from: null, to: null}};
            if (f.field === 'type') {
                filterOpt.filterField = 'type';
                filterOpt.messageType = f.value;
            }
            if (f.field === 'text') {
                filterOpt.filterField = 'text';
                filterOpt.messageText = f.value;
            }
            if (f.field === 'value') {
                filterOpt.filterField = 'value';
                let values = f.value.split(';');
                filterOpt.messageValue.from = +values[0];
                filterOpt.messageValue.to = +values[1];
            }
            if (f.field === 'timestamp') {
                filterOpt.filterField = 'timestamp';
                let values = f.value.split(';');;
                filterOpt.messageTimestamp.from = new Date(values[0]).toISOString().split('T')[0];
                filterOpt.messageTimestamp.to = new Date(values[1]).toISOString().split('T')[0];
            }
            this.filterOptions.push(filterOpt);
        }
        if (this.filterOptions.length <= 3)
            this.filterOptions.push({filterField: '', messageType: '', messageText: '', messageValue: {from: 0, to: 0}, messageTimestamp: {from: null, to: null}});
    }
    

    setFilterField = (filter: FilterOption, target: any) => {
        filter.filterField = target.value;
    }

    setMessageType = (filter: FilterOption, target: any) => {
        filter.messageType = target.value;
    }

    addFilter = () => {
        this.filterOptions.push({filterField: '', messageType: '', messageText: '', messageValue: {from: 0, to: 0}, messageTimestamp: {from: null, to: null}})
    }

    apply = () => {
        let filter: Filter[] = [];
        let typePushed = false;
        let textPushed = false;
        let valuePushed = false;
        let timestampPushed = false;
        for (let option of [...this.filterOptions].reverse()) {
            if (typePushed && textPushed && valuePushed && timestampPushed) break;
            if (option.filterField === 'type' && option.messageType !== '' && !typePushed) {
                typePushed = true;
                filter.push({field: option.filterField, value: option.messageType});
            }
            if (option.filterField === 'text' && option.messageText !== '' && !textPushed) {
                textPushed = true;
                filter.push({field: option.filterField, value: option.messageText});
            }
            if (option.filterField === 'value' && option.messageValue.from <= option.messageValue.to && !valuePushed) {
                valuePushed = true;
                filter.push({field: option.filterField, value: `${option.messageValue.from};${option.messageValue.to}`});
            }
            if (option.filterField === 'timestamp' && option.messageTimestamp.from && option.messageTimestamp.to && option.messageTimestamp.from <= option.messageTimestamp.to && !timestampPushed) {
                timestampPushed = true;
                filter.push({field: option.filterField, value: `${option.messageTimestamp.from};${option.messageTimestamp.to}`})
            }
        }
        console.log(filter)
        this.applyFilter.emit(filter.reverse());
    } 

    remove = (option: FilterOption) => {
        this.filterOptions = this.filterOptions.filter(opt => opt !== option);
    }
  }