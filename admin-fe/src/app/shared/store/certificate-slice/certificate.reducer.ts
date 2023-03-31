import { createFeatureSelector } from "@ngrx/store";
import { EditExtensionAction, EditExtensionActionType } from "./certificate.actions";

export interface CertificateStateType {
    editedExtension: any;
    showEditingModal: boolean;
}

const initialState: CertificateStateType = {
  editedExtension: undefined,
  showEditingModal: false,
};

export const editCertificateReducer = (
  state: CertificateStateType = initialState,
  action: EditExtensionAction
) => {
  switch (action.type) {
    case EditExtensionActionType.SET:      
      return {
        ...state,
        editedExtension: action.extension,
      };

    case EditExtensionActionType.RESET:
      return {
        ...state,
        editedExtension: undefined,
      };
    case EditExtensionActionType.TOGGLE_MODAL:
      
      return {
        ...state,
        showEditingModal: !state.showEditingModal,
      };
    default: {
      return state;
    }
  }
};

export const selectCertificateFeature = createFeatureSelector<CertificateStateType>('certificate');
