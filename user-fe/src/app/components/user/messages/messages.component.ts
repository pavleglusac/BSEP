import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faFilter } from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { Device, Message, RealEstate } from 'src/app/model/myhouse';
import { MyHouseService } from 'src/app/services/myhouse.service';
import { StoreType } from 'src/app/shared/store/types';
import { MessagesTableComponent } from './messages-table/messages-table.component';
import { Filter, FilterComponent } from './filter/filter.component';
import { RealEstateAction, RealEstateActionType } from 'src/app/shared/store/real-estate-slice/real-estate.actions';
import { User } from 'src/app/model/user';

const AMOUNT: number = 12;

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css'],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, MessagesTableComponent, FilterComponent]
})


export class MessagesComponent implements OnInit{
  user: User | null = null;
  realEstates: RealEstate[] = [];
  selectedRealEstate: RealEstate | null = null;
  selectedDevice: Device | null = null;
  messages: Message[] | null = null;
  page: number = 1;
  pageInfo: any = null;
  selectedDeviceVal: string = ''

  faFilter: IconDefinition = faFilter;
  openFilter: boolean = false;
  filter: Filter[] = []; 

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
    this.selectedDevice = null;
    this.selectedDeviceVal = '';
    return this.realEstates.filter(state => state.id === id)[0];
   }

  getSelectedDevice = (realEstate: RealEstate, target: any) => {
    let id = target.value;
    this.selectedDeviceVal = id;
    return realEstate.devices!.filter(device => device.id === id)[0];
  }

  
  search = () => {
    let filterString = '';
    for (let f of this.filter) {
      filterString += f.field + '=' + f.value + '&';
    }
    filterString = filterString.slice(0, -1);
    this.houseService.searchMessages(
      this.selectedDevice!.id!,
      this.page - 1,
      AMOUNT,
      filterString,
      (value: any) => {
        this.messages = value.content;
        this.pageInfo = value;
      },
      (err: any) => this.toastr.error(err.message)
      )
    }
    
    prevPage = () => {
      if (this.page > 1) {
        this.page -= 1;
        this.search();
    }
  }
  
  nextPage = () => {
    if (this.page < this.pageInfo.totalPages) {
      this.page += 1;
      this.search();
    }
  }
  
  pagination = (func: string) => {
    if (func === "prevPage") {
      this.prevPage();
    } else {
      this.nextPage();
    }
  }
  
  toggleFilter = () => {
    this.openFilter = !this.openFilter;
  }

  applyFilter = (filters: Filter[]) => {
    this.filter = filters
    this.toggleFilter();
  }
}
