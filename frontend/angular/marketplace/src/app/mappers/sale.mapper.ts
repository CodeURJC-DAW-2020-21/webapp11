import {Injectable} from '@angular/core';
import {Sale} from '../models/sale.model';

@Injectable({
  providedIn: 'root'
})
export class SaleMapper {

  constructor() { }

  asSale(response: any): Sale {
    return new Sale(
      response.product_id,
      response.bulk_amount,
      response.discount,
      response.discount_price,
      this.formatToIso(response.start_date),
      this.formatToIso(response.expiry_date),
      response.ram,
      response.cores,
      response.storage,
      response.transfer
    );
  }

  asUpdateRequest(sale: Sale): any {
    return {
      product_id: sale.productId,
      start: this.formatToSpanish(sale.startDate),
      stop: this.formatToSpanish(sale.expiryDate),
      discount: sale.discountPercentage,
      amount: sale.bulkAmount,
      enabled: true
    };
  }

  formatToIso(date: string): string {
    let day: any = Number(date.split('-')[0]);
    day = (day < 10 ? '0' : '') + day;
    let month: any = Number(date.split('-')[1]);
    month = (month < 10 ? '0' : '') + month;
    const year = Number(date.split('-')[2]);
    return year + '-' + month + '-' + day;
  }

  formatToSpanish(date: string): string {
    let day: any = date.split('-')[2];
    day = (day < 10 ? '0' : '') + day;
    let month: any = date.split('-')[1];
    month = (month < 10 ? '0' : '') + month;
    const year = date.split('-')[0];
    return day + '-' + month + '-' + year;
  }

}
