import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-alarm',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './add-alarm.component.html',
  styles: [
  ]
})
export class AddAlarmComponent {
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
      Validators.pattern(/^\w+(?: \w+)*$/),
    ]),
  });

  deviceTypes: string[] = [
    "THERMOMETER",
    "MOTION_DETECTOR",
    "LOCK",
    "LAMP",
    "GATE",
  ]

  operatorValues: string[] = [
    "==",
    ">=",
    "<=",
    ">",
    "<",
  ]

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
  ]

  deviceType: string = this.deviceTypes[0];
  errorMessage: string = '';
  value: number = 0;
  valueError: string = '';
  operatorValue: string = this.operatorValues[0];
  num: number = 0;
  numError: string = '';
  operatorNum: string = this.operatorValues[0];
  windowValue: number = 0;
  windowValueError: string = '';
  windowUnit: string = '';

  onSubmit(): void {

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

  deviceTypeChanged(event: any) {
    this.deviceType = event.target.value;
  }

  operatorValueChanged(event: any) {
    this.operatorValue = event.target.value;
  }
  
  operatorNumChanged(event: any) {
    this.operatorNum = event.target.value;
  }
  
  windowUnitChanged(event: any) {
    this.windowUnit = event.target.value;
  }

  checkIfValueIsNumber() {
    this.checkIfNum(this.value, (v: string) => {this.valueError = v})
  }

  checkIfNumIsNumber() {
    this.checkIfNum(this.num, (v: string) => {this.numError = v})
  }

  checkIfWindowValueIsNumber() {
    this.checkIfNum(this.windowValue, (v: string) => {this.windowValueError = v})
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
