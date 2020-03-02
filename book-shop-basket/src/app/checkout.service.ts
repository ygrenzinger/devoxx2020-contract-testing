import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs/internal/Observable";
import { Order } from './typings';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private serviceUrl  = '/v1/checkouts';

  constructor(private http: HttpClient) {
  }

  checkout(order: Order): Observable<Order>{
    return this.http.post<Order>(this.serviceUrl, order)
  }

}
