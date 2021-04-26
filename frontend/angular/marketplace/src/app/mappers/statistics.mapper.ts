import {Injectable} from '@angular/core';
import {Statistics} from '../models/statistics.model';

@Injectable({
  providedIn: 'root'
})
export class StatisticsMapper {

  constructor() { }

  asStatistics(response: any): Statistics {
    const categoryPurchases = { };
    for (const purchase of response.category_purchases) {
      const category = Object.getOwnPropertyNames(purchase)[0];
      categoryPurchases[category] = purchase[category];
    }
    return new Statistics(
      response.accumulated_capital,
      response.week_sales,
      categoryPurchases
    );
  }

}
