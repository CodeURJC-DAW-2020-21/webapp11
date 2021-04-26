import { Injectable } from '@angular/core';
import {Product} from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductMapper {

  constructor() { }

  asProducts(response: any): Product[] {
    return response.map((item: any) => {
      return new Product(
        item.id,
        item.category,
        item.price,
        item.hourly_price,
        item.ram,
        item.cores,
        item.storage,
        item.transfer
      );
    });
  }

}
