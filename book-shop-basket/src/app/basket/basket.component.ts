import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Book } from '../typings';
import { InventoryService } from '../inventory.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent implements OnInit {

  book: Book[] = []

  constructor(private inventoryService: InventoryService) { }

  ngOnInit() {
    this.inventoryService.allBooks().subscribe(book => this.book = book)
  }

}
