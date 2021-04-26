import { Component, OnInit } from '@angular/core';
import {ChartDataSets, ChartOptions, ChartType} from 'chart.js';
import { Color, Label } from 'ng2-charts';
import {StatisticsService} from '../../../services/statistics.service';
import {Error} from '../../../models/error.model';

@Component({
  selector: 'app-weekly-sales-chart',
  templateUrl: './weekly-sales-chart.component.html',
  styleUrls: ['./weekly-sales-chart.component.css']
})
export class WeeklySalesChartComponent implements OnInit {

  public options: ChartOptions = { responsive: true };
  public labels: Label[] = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  public data: ChartDataSets[] = [{ data: [], label: 'Sales in a day' }];
  public type: ChartType = 'line';
  public legend = true;
  public colors: Color[] = [{ backgroundColor: 'rgb(21,76,121)', borderColor: 'rgb(6,57,112)' }];
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
      this.data = [{ data: response.weekSales, label: 'Sales in a day' }];
    });
  }

}
