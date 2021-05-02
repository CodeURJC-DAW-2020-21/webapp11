import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product.model';
import {Error} from '../../models/error.model';
import {OrderService} from '../../services/order.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Sale} from '../../models/sale.model';
import {SaleService} from '../../services/sale.service';

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.css']
})
export class PricingComponent implements OnInit {

  private categoryToProducts: Map<string, Product[]> = new Map<string, Product[]>();

  public selectedSale = 'onetime';

  public oneTimeSale: Sale = new Sale();
  public accumulativeSale: Sale = new Sale();

  public selectedProduct: Product = new Product();
  public selectedCategory = '';
  public selectedProductIndex = 0;
  public lastProductIndex = 0;
  public errorMessage = '';

  constructor(
    private activated: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private orderService: OrderService,
    private saleService: SaleService
  ) {
    this.activated.queryParams.subscribe(params => {
      this.selectedCategory = params.category == null ? '' : params.category;
      this.refreshCategory();
    });
  }

  ngOnInit(): void {
    this.loadAllProducts();
    this.loadSales();
  }

  loadSales(): void {
    this.saleService.findSale('onetime').subscribe((response) => {
      if (response instanceof Error) { return; }
      this.oneTimeSale = response;
    });
    this.saleService.findSale('accumulative').subscribe((response) => {
      if (response instanceof Error) { return; }
      this.accumulativeSale = response;
    });
    this.startAnnouncements();
  }

  loadAllProducts(): void {
    const observable = this.productService.findAllProducts();
    observable.subscribe((response) => {
      if (response instanceof Error) {
        this.errorMessage = response.message;
      } else {
        for (const product of response) {
          if (!this.categoryToProducts.has(product.category)) {
            this.categoryToProducts.set(product.category, []);
          }
          this.categoryToProducts.get(product.category).push(product);
        }
      }
      this.loadDefaultState();
    });
  }

  loadDefaultState(): void {
    if (this.categoryToProducts.size === 0) {
      return;
    }
    if (this.selectedCategory === '') {
      this.selectedCategory = this.categoryToProducts.keys().next().value;
    }
    this.refreshCategory();
  }

  refreshCategory(): void {
    this.switchToCategory(this.selectedCategory);
  }

  switchToCategory(category: string): void {
    if (!this.categoryToProducts.has(category)) {
      return;
    }
    this.selectedCategory = category;
    this.selectedProduct = this.categoryToProducts.get(category)[0];
    this.selectedProductIndex = 0;
    this.lastProductIndex = this.categoryToProducts.get(category).length - 1;
  }

  selectProduct(): void {
    const productIndex = this.selectedProductIndex;
    this.selectedProductIndex = productIndex;
    this.selectedProduct = this.categoryToProducts.get(this.selectedCategory)[productIndex];
  }

  purchaseProduct(): void {
    const selectedProduct = this.getSelectedProduct();
    const observable = this.orderService.placeOrder(selectedProduct.id);
    observable.subscribe(
      (success: boolean) => {
        this.router.navigate(['/services']).then();
      },
      (error: Error) => {
        if (error.isUnauthorized()) { this.router.navigate(['/login']).then(); return; }
        this.router.navigate(['/error']).then();
      }
    );
  }

  getSelectedProduct(): Product {
    return this.categoryToProducts.get(this.selectedCategory)[this.selectedProductIndex];
  }

  startAnnouncements(): void {
    setInterval(() => { this.nextAnnouncement(); }, 10000);
  }

  nextAnnouncement(): void {
    let nextSale = 'accumulative';
    if (this.selectedSale === 'onetime' && this.accumulativeSale.productId !== -1) {
      nextSale = 'accumulative';
    }
    else if (this.selectedSale === 'accumulative' && this.oneTimeSale.productId !== -1) {
      nextSale = 'onetime';
    }
    else {
      if (this.oneTimeSale.productId !== -1) {
        nextSale = 'onetime';
      }
      else if (this.accumulativeSale.productId !== -1) {
        nextSale = 'accumulative';
      }
      else {
        nextSale = '';
      }
    }
    this.selectedSale = nextSale;
  }

}
