import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { AlarmService } from 'src/app/services/alarm.service';

@Component({
  selector: 'app-add-alarm-rule',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './add-alarm-rule.component.html',
  styles: [
  ]
})
export class AddAlarmRuleComponent {
  faAngleDown: IconDefinition = faAngleDown;

  alarmForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      Validators.maxLength(48),
      Validators.pattern(/^\w+(?: \w+)*$/),
    ]),
    messageType: new FormControl('', [
      Validators.required,
      Validators.maxLength(32),
      Validators.pattern(/^\w+(?: \w+)*$/),
    ]),
    textRegex: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    alarmText: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
      Validators.pattern(/^[\w.!]+(?: [\w.!]+)*$/),
    ]),
  });

  deviceTypes: string[] = [
    "ALL",
    "THERMOMETER",
    "MOTION_DETECTOR",
    "LOCK",
    "LAMP",
    "GATE",
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

  enableTextRegex: boolean = false;
  enableValue: boolean = false;
  enableWindow: boolean = false;

  deviceType: string = this.deviceTypes[0];
  value: number = 0;
  valueError: string = '';
  operatorValue: string = this.operatorValues[0];
  num: number = 0;
  numError: string = '';
  operatorNum: string = this.operatorValues[0];
  windowValue: number = 0;
  windowValueError: string = '';
  windowUnit: string = this.droolsTimeUnits[0].value;

  errorMessage: string = '';

  constructor(private alarmService: AlarmService, private toastr: ToastrService) {}

  onSubmit(): void {
    if (this.isFormValid()) {
      this.alarmService.addAlarmRule(
        { 
          name: this.name?.value!,
          alarmText: this.alarmText?.value!,
          messageType: this.messageType?.value!,
          num: this.num,
          operatorNum: this.operatorNum,
          deviceType: this.deviceType === "ALL" ? undefined : this.deviceType,
          textRegex: this.enableTextRegex ? this.textRegex?.value! : undefined,
          window: this.enableWindow ? `${this.windowValue}${this.windowUnit}` : undefined,
          value: this.enableValue ? this.value : undefined,
          operatorValue: this.enableValue ? this.operatorValue : undefined,
        }, 
        () => {
          this.toastr.success("Success!");
          window.location.href = '/admin/alarms';
        },
        (err) => this.toastr.error(err.message))
    }
  }

  isFormValid(): boolean {
    if (this.name?.invalid || this.alarmText?.invalid || this.messageType?.invalid) {
      this.alarmForm.markAllAsTouched();
      return false;
    }
    if (this.enableTextRegex && this.textRegex?.invalid) {
      this.alarmForm.markAllAsTouched();
      return false;
    }
    if (this.enableValue && this.value === null) {
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

  get name() {
    return this.alarmForm.get('name');
  }

  get messageType() {
    return this.alarmForm.get('messageType');
  }
  
  get textRegex() {
    return this.alarmForm.get('textRegex');
  }

  get alarmText() {
    return this.alarmForm.get('alarmText');
  }

  deviceTypeChanged(event: any) {
    this.deviceType = event.target.value.toUpperCase().replaceAll(' ', '_');
  }
  
  operatorValueChanged(event: any) {
    this.operatorValue = event.target.value;
  }
  
  operatorNumChanged(event: any) {
    this.operatorNum = event.target.value;
  }
  
  windowUnitChanged(event: any) {
    this.windowUnit = this.droolsTimeUnits.filter(x => x.name === event.target.value)[0].value;
  }

  checkIfValueIsNumber() {
    if (this.enableValue) {
      this.checkIfNum(this.value, (v: string) => {this.valueError = v});
    }
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
