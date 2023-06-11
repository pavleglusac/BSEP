import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { Report } from "src/app/model/report";

@Component({
    selector: 'app-report-entry',
    templateUrl: './report-entry.component.html',
    styleUrls: [],
    standalone: true,
    imports: [CommonModule]
  })
  
  
  export class ReportEntryComponent {
    @Input() report!: Report;
  }