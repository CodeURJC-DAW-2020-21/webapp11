import { Injectable } from '@angular/core';
import {Product} from '../models/product.model';
import {User} from '../models/user.model';
import {Order} from '../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderMapper {

  constructor() { }

  asOrders(response: any): Order[] {
    return response.map((item: any) => this.asOrder(item));
  }

  asOrder(item: any): Order {
    return new Order(
      item.order_id,
      item.category,
      item.purchase_date,
      item.expiration_date,
      item.is_expired,
      item.ram,
      item.cores,
      item.storage,
      item.transfer
    );
  }

}
