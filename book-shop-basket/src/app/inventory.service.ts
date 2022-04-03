import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { Book } from './typings';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private serviceUrl = '/v1/books';

  constructor(private http: HttpClient) {
  }

  allBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.serviceUrl);
  }

}
