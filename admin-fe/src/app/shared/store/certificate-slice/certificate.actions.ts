import { Action } from '@ngrx/store';

export enum EditExtensionActionType {
    SET = 'SET',
    RESET = 'RESET',
    TOGGLE_MODAL = 'TOGGLE_MODAL',
  }

export class EditExtensionAction implements Action {
  constructor(readonly type: string, readonly extension: any) {}
}
