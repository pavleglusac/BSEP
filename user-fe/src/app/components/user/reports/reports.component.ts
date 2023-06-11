import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faFilter } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { ToastrService } from "ngx-toastr";
import { Device, RealEstate } from "src/app/model/myhouse";
import { Report } from "src/app/model/report";
import { User } from "src/app/model/user";
import { MyHouseService } from "src/app/services/myhouse.service";
import { RealEstateAction, RealEstateActionType } from "src/app/shared/store/real-estate-slice/real-estate.actions";
import { StoreType } from "src/app/shared/store/types";
import { ReportEntryComponent } from "./report-entry/report-entry.component";

@Component({
    selector: 'app-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['./reports.component.css'],
    standalone: true,
    imports: [CommonModule, FormsModule, FontAwesomeModule, ReportEntryComponent]
  })
  
  
  export class ReportsComponent implements OnInit{
    user: User | null = null;
    realEstates: RealEstate[] = [];
    selectedRealEstate: RealEstate | null = null;
    selectedDevice: Device | null = null;
    reports: Report[] = [];
    faFilter: IconDefinition = faFilter;
    from: Date | null = null;
    to: Date | null = null;
    typeField: string = '';

    constructor( private store: Store<StoreType>, private houseService: MyHouseService, private toastr: ToastrService) {
        store.subscribe(state => {
          this.user = state.loggedUser.user;
          this.realEstates = state.realEstates.realEstates;
        })
    }

    ngOnInit(): void {
        this.houseService.loadRealEstates(
            this.user!.email,
            (realEstates: RealEstate[]) => {
                this.store.dispatch(new RealEstateAction(RealEstateActionType.SET_REAL_ESTATES, realEstates)); 
            },
            (err) => this.toastr.error(err.message)
        );
    }

    getSelectedRealEstate = (target: any) => {
        let id = target.value;
        return this.realEstates.filter(state => state.id === id)[0];
       }
    
    getSelectedDevice = (realEstate: RealEstate, target: any) => {
        let id = target.value;
        let device: Device = realEstate.devices!.filter(device => device.id === id)[0]
        this.typeField = device.id!;
        return device;
    }

    show = () => {
        let type: string = this.selectedDevice ? 'device' : 'realEstate';
        let id: string = this.selectedDevice ? this.selectedDevice!.id! : this.selectedRealEstate!.id;
        if ((this.from && this.to && this.from >= this.to) || (this.from && !this.to) || (!this.from && this.to)) {
            this.from = null;
            this.to = null;
        }
        this.houseService.getReports(
            id,
            this.from,
            this.to,
            type,
            (value: any) => {
                this.reports = value;
            },
            (err: any) => {
                this.toastr.error(err.message);
            }
        )
    }

    get filterSelected(): boolean {
        return this.selectedDevice !== null || (this.from !== null && this.to != null)
    }

    clearFilter = () => {
        if (this.filterSelected){
            this.selectedDevice = null;
            this.from = null;
            this.to = null;
            this.typeField = '';
        }
    }

  }