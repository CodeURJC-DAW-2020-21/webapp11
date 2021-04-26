import { Injectable } from '@angular/core';
import {Product} from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductMapper {

  constructor() { }

  asProducts(response: any): Product[] {
    return response.content.map((item: any) => {
      return new Product(
        item.order_id,
        item.category,
        item.purchase_date,
        item.expiration_date,
        item.ram,
        item.cores,
        item.storage,
        item.transfer
      );
    });
  }

}
