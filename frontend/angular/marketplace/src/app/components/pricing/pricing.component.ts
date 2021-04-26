import { Component, OnInit } from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product.model';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
  styleUrls: ['./pricing.component.css']
})
export class PricingComponent implements OnInit {

  private categoryToProducts: Map<string, Product[]> = new Map<string, Product[]>();

  public selectedProduct: Product = new Product();
  public selectedCategory = '';
  public selectedProductIndex = 0;
  public lastProductIndex = 0;
  public errorMessage = '';

  constructor(
    private productService: ProductService,
  ) { }

  ngOnInit(): void {
    this.loadAllProducts();
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
    if (this.categoryToProducts.size === 0) { return; }
    this.selectedCategory = this.categoryToProducts.keys().next().value;
    this.switchToCategory(this.selectedCategory);
  }

  switchToCategory(category: string): void {
    if (!this.categoryToProducts.has(category)) { return; }
    this.selectedCategory = category;
    this.selectedProduct = this.categoryToProducts.get(category)[0];
    this.selectedProductIndex = 0;
    this.lastProductIndex = this.categoryToProducts.get(category).length - 1;
    console.log(this.selectedCategory);
  }

  selectProduct(): void {
    const productIndex = this.selectedProductIndex;
    console.log(productIndex);
    this.selectedProductIndex = productIndex;
    this.selectedProduct = this.categoryToProducts.get(this.selectedCategory)[productIndex];
  }

}
