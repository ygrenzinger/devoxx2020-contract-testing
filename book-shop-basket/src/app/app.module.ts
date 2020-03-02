import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { BasketComponent } from './basket/basket.component';
import { CheckoutService } from './checkout.service';
import { HttpClientModule } from '@angular/common/http';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    BasketComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    HttpClientModule
  ],
  providers: [CheckoutService],
  bootstrap: [AppComponent]
})
export class AppModule { }
