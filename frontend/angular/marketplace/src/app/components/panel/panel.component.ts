import { Component, OnInit } from '@angular/core';
import {Statistics} from '../../models/statistics.model';
import {StatisticsService} from '../../services/statistics.service';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  public accumulatedCapital = 0;

  constructor(private statisticsService: StatisticsService) { }

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    const observable = this.statisticsService.findStatistics();
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulatedCapital = response.accumulatedCapital;
    });
  }

}
