import { Component, OnInit } from '@angular/core';
import {Order} from '../../models/order.model';
import {OrderService} from '../../services/order.service';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrls: ['./services.component.css']
})
export class ServicesComponent implements OnInit {

  private currentPage = 1;
  private loadAmount = 10;
  public loadedOrders: Order[] = [];

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadNextOrders();
  }

  loadNextOrders(): void {
    this.loadUsers(this.currentPage, this.loadAmount);
  }

  loadUsers(page: number, amount: number): void {
    const observable = this.orderService.findOrders(page, amount);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      for (const order of response) {
        this.loadedOrders.push(order);
      }
      this.currentPage++;
    });
  }
}
