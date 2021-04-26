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

  constructor(
    private productService: ProductService,
    private products: Product[]
  ) { }

  ngOnInit(): void {

  }

  displayAllProducts(): void {
    const observable = this.productService.findAllProducts();
    observable.subscribe((response) => {
      if (response instanceof Error) {
        // Display error
      } else {
        this.products = response;
      }
    });
  }

}
