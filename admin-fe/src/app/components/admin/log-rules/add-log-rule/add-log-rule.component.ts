import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { LogService } from 'src/app/services/log.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-log-rule',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './add-log-rule.component.html',
  styles: [
  ]
})
export class AddLogRuleComponent {
  faAngleDown: IconDefinition = faAngleDown;

  logRulesForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(48),
      Validators.pattern(/^\w+(?: \w+)*$/),
    ]),
    alarmText: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
      Validators.pattern(/^[\w.!]+(?: [\w.!]+)*$/),
    ]),
    actionRegex: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    detailsRegex: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    ipAddressRegex: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
  });

  logTypes: string[] = [
    "ALL",
    "ERROR",
    "WARNING",
    "SUCCESS",
    "INFO"
  ];

  operatorValues: string[] = [
    "==",
    ">=",
    "<=",
    ">",
    "<",
  ];

  droolsTimeUnits = [
    {
      value: 'ms',
      name: 'Milliseconds'
    },
    {
      value: 's',
      name: 'Seconds'
    },
    {
      value: 'm',
      name: 'Minutes'
    },
    {
      value: 'h',
      name: 'Hours'
    },
    {
      value: 'd',
      name: 'Days'
    },
  ];

  enableActionRegex: boolean = false;
  enableDetailsRegex: boolean = false;
  enableIpAddressRegex: boolean = false;
  enableWindow: boolean = false;

  logType: string = this.logTypes[0];
  num: number = 0;
  numError: string = '';
  operatorNum: string = this.operatorValues[0];
  windowValue: number = 0;
  windowValueError: string = '';
  windowUnit: string = this.droolsTimeUnits[0].value;
  usernames: string[] = [];
  username: string = '';

  errorMessage: string = '';
  
  constructor(private logService: LogService, private toastr: ToastrService) {}

  onSubmit(): void {
    if (this.isFormValid()) {

    }
  }

  isFormValid(): boolean {
    if (this.name?.invalid || this.alarmText?.invalid) {
      this.logRulesForm.markAllAsTouched();
      return false;
    }
    if (this.enableActionRegex && this.actionRegex?.invalid) {
      this.logRulesForm.markAllAsTouched();
      return false;
    }
    if (this.enableDetailsRegex && this.detailsRegex?.invalid) {
      this.logRulesForm.markAllAsTouched();
      return false;
    }
    if (this.enableIpAddressRegex && this.ipAddressRegex?.invalid) {
      this.logRulesForm.markAllAsTouched();
      return false;
    }
    if (this.enableWindow && this.windowValue === null) {
      return false;
    }
    if (this.num === null) {
      return false;
    }
    return true;
  }

  handleUsernameInput(): void {
    const value = this.username.slice(0, this.username.length - 1).replaceAll(',', '') + this.username.slice(this.username.length - 1, this.username.length);
    if (value && value.length > 1 && value.slice(value.length - 1, value.length) === ',') {
      this.username = '';
      const toAdd = value.slice(0, value.length - 1);
      if (!this.usernames.includes(toAdd)) {
        this.usernames.push(toAdd);
      }
    }
  }

  removeUsername(uname: string): void {
    this.usernames = this.usernames.filter(x => x !== uname)
  }

  get name() {
    return this.logRulesForm.get('name');
  }

  get actionRegex() {
    return this.logRulesForm.get('actionRegex');
  }
  
  get detailsRegex() {
    return this.logRulesForm.get('detailsRegex');
  }
  
  get ipAddressRegex() {
    return this.logRulesForm.get('ipAddressRegex');
  }

  get alarmText() {
    return this.logRulesForm.get('alarmText');
  }

  logTypeChanged(event: any) {
    this.logType = event.target.value.toUpperCase().replaceAll(' ', '_');
  }

  operatorNumChanged(event: any) {
    this.operatorNum = event.target.value;
  }

  windowUnitChanged(event: any) {
    this.windowUnit = this.droolsTimeUnits.filter(x => x.name === event.target.value)[0].value;
  }

  checkIfNumIsNumber() {
    this.checkIfNum(this.num, (v: string) => {this.numError = v});
  }

  checkIfWindowValueIsNumber() {
    if (this.enableWindow) {
      this.checkIfNum(this.windowValue, (v: string) => {this.windowValueError = v});
    }
  }

  checkIfNum(value: number, errorCallback: any) {
    if (value === null) {
      errorCallback('Not a number')
    } else {
      try {
        Number(value);
        errorCallback('')
      } catch (e) {
        errorCallback('Not a number')
      }
    }
  }
}
