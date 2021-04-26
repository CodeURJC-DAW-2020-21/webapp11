import { Injectable } from '@angular/core';
import {TokenService} from './token.service';
import {Observable, throwError} from 'rxjs';
import {Product} from '../models/product.model';
import { Error } from '../models/error.model';
import {HttpClient} from '@angular/common/http';
import {ProductMapper} from './product.mapper';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private ROUTE = 'https://localhost/api/products';

  constructor(
    private http: HttpClient,
    private productMapper: ProductMapper
  ) { }

  findAllProducts(): Observable<Product[] | Error> {
    return new Observable<Product[] | Error>((subscriber) => {
      this.http.get<any>(this.ROUTE)
        .subscribe(
          (data) => {
            const products = this.productMapper.asProducts(data.content);
            subscriber.next(products);
          },
          (error) => {
            const json = error.error;
            if ('content' in json) {
              subscriber.next(new Error('answered', json.content));
            } else {
              const message = 'Could not connect to the server. Is the server down?';
              subscriber.next(new Error('unanswered', message));
            }
          }
        );
    });
  }

}
