import { Component, OnInit } from '@angular/core';
import {User} from '../../models/user.model';
import {StatisticsService} from '../../services/statistics.service';
import {Error} from '../../models/error.model';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  private currentPage = 1;
  private loadAmount = 10;

  public accumulatedCapital = 0;
  public loadedUsers: User[] = [];

  constructor(
    private statisticsService: StatisticsService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.loadStatistics();
    this.loadNextUsers();
  }

  loadStatistics(): void {
    const observable = this.statisticsService.findStatistics();
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulatedCapital = response.accumulatedCapital;
    });
  }

  loadNextUsers(): void {
    this.loadUsers(this.currentPage, this.loadAmount);
  }

  loadUsers(page: number, amount: number): void {
    const observable = this.userService.findUsers(page, amount);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      for (const user of response) {
        this.loadedUsers.push(user);
      }
      this.currentPage++;
    });
  }

  enableUser(user: User): void {
    console.log(JSON.stringify(user));
    return;
  }

  disableUser(user: User): void {
    console.log(JSON.stringify(user));
    return;
  }

  viewUser(user: User): void {
    console.log(JSON.stringify(user));
    return;
  }

}
