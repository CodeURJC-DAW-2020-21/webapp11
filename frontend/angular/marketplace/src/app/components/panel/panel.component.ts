import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user.model';
import {StatisticsService} from '../../services/statistics.service';
import {Error} from '../../models/error.model';
import {UserService} from '../../services/user.service';
import {Sale} from '../../models/sale.model';
import {SaleService} from '../../services/sale.service';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  private currentPage = 1;
  private loadAmount = 10;

  public todayDate = new Date();
  public accumulatedCapital = 0;
  public loadedUsers: User[] = [];

  public clientEnabledMessage = '';
  public clientDisabledMessage = '';

  public oneTimeSale = new Sale();
  public oneTimeSaleUpdatedMessage = '';
  public oneTimeSaleDisabledMessage = '';
  public oneTimeProductId = -1;

  public accumulativeSale = new Sale();
  public accumulativeSaleUpdatedMessage = '';
  public accumulativeSaleDisabledMessage = '';
  public accumulativeProductId = -1;


  public products: Product[] = [];

  constructor(
    private router: Router,
    private statisticsService: StatisticsService,
    private userService: UserService,
    private saleService: SaleService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.loadStatistics();
    this.loadNextUsers();
    this.loadSales();
    this.loadProducts();
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

  loadSales(): void {
    this.saleService.findSale('onetime').subscribe((response) => {
      if (response instanceof Error) { return; }
      this.oneTimeSale = response;
      this.oneTimeProductId = response.productId;
    });
    this.saleService.findSale('accumulative').subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulativeSale = response;
      this.accumulativeProductId = response.productId;
    });
  }

  loadProducts(): void {
    this.productService.findAllProducts().subscribe((response) => {
      if (response instanceof Error) { return; }
      for (const product of response) {
        this.products.push(product);
      }
    });
  }

  enableUser(user: User, userIndex: number): void {
    const observable = this.userService.enableUser(user.id);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.loadedUsers[userIndex] = response;
      this.clientEnabledMessage = `The user ${user.firstName} ${user.surname} account has been enabled!`;
      this.clientDisabledMessage = '';
    });
  }

  disableUser(user: User, userIndex: number): void {
    const observable = this.userService.disableUser(user.id);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.loadedUsers[userIndex] = response;
      this.clientDisabledMessage = `The user ${user.firstName} ${user.surname} account has been disabled!`;
      this.clientEnabledMessage = '';
    });
  }

  viewUser(user: User): void {
    this.router.navigate(['/profile']).then();
    return;
  }

  updateOnetimeSale(): void {
    const observable = this.saleService.updateSale('onetime', this.oneTimeSale);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.oneTimeSale = response;
      this.oneTimeProductId = response.productId;
      this.oneTimeSaleUpdatedMessage = 'The sale has been successfully saved';
      this.oneTimeSaleDisabledMessage = '';
    });
  }

  updateAccumulativeSale(): void {
    const observable = this.saleService.updateSale('accumulative', this.accumulativeSale);
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulativeSale = response;
      this.accumulativeProductId = response.productId;
      this.accumulativeSaleUpdatedMessage = 'The sale has been successfully saved';
      this.accumulativeSaleDisabledMessage = '';
    });
  }

  disableOnetimeSale(): void {
    const observable = this.saleService.disableSale('onetime');
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.oneTimeProductId = -1;
      this.oneTimeSaleDisabledMessage = 'The one time discount sale has been successfully disabled!';
      this.oneTimeSaleUpdatedMessage = '';
    });
  }

  disableAccumulativeSale(): void {
    const observable = this.saleService.disableSale('accumulative');
    observable.subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulativeProductId = -1;
      this.accumulativeSaleDisabledMessage = 'The accumulative discount sale has been successfully disabled!';
      this.accumulativeSaleUpdatedMessage = '';
    });
  }

}
