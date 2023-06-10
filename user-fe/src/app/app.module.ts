import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';
import { StoreModule } from '@ngrx/store';
import { metaReducers } from './local-storage.service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './shared/interceptors/http.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { loggedUserReducer } from './shared/store/logged-user-slice/logged-user.reducer';
import { realEstateReducer } from './shared/store/real-estate-slice/real-estate.reducer';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-center',
      preventDuplicates: true,
    }),
    AppRoutingModule,
    BrowserAnimationsModule,
    StoreModule.forRoot(
      {
        loggedUser: loggedUserReducer,
        realEstates: realEstateReducer
      },
      { metaReducers }
    ),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
