import {Component, OnInit, } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import {Color, Label} from 'ng2-charts';
import {StatisticsService} from '../../../services/statistics.service';
import {Error} from '../../../models/error.model';

@Component({
  selector: 'app-category-purchases-chart',
  templateUrl: './category-purchases-chart.component.html',
  styleUrls: ['./category-purchases-chart.component.css']
})
export class CategoryPurchasesChartComponent implements OnInit {

  public options: ChartOptions = { responsive: true };
  public labels: Label[] = [];
  public data: Array<number> = [];
  public type: ChartType = 'pie';
  public legend = true;
  public colors: Color[] = [];
  public plugins = [];

  constructor(private statisticsService: StatisticsService) {
  }

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    const observable = this.statisticsService.findStatistics();
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.labels = Object.keys(response.categoryPurchases);
      this.data = Object.values(response.categoryPurchases);
      this.colors = this.getColors(this.data.length);
    });
  }

  getColors(amount: number): Color[] {
    return [{
      borderColor: 'white',
      backgroundColor: this.getPseudoRandomHexColors(amount),
    }];
  }

  getPseudoRandomHexColors(amount: number): string[] {
    const predefinedColors = [ '#002B4B', '#0067B5', '#32556E' ];
    let currentColor = 1;
    const nextColor = () => {
      let colorIndex = currentColor % predefinedColors.length;
      if (colorIndex > predefinedColors.length - 1) { colorIndex = 0; }
      currentColor++;
      return predefinedColors[colorIndex];
    };
    const resultColors = [];
    let colorsNeeded = amount;
    while (colorsNeeded !== 0) {
      resultColors.push(nextColor());
      colorsNeeded--;
    }
    return resultColors;
  }

}
