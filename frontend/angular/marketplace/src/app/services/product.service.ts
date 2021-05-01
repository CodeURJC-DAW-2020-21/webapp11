import { Injectable } from '@angular/core';
import {TokenService} from './token.service';
import {Observable, throwError} from 'rxjs';
import {Product} from '../models/product.model';
import { Error } from '../models/error.model';
import {HttpClient} from '@angular/common/http';
import {ProductMapper} from '../mappers/product.mapper';
import {Constants} from '../configs/constants';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/products`;

  constructor(
    private httpClient: HttpClient,
    private productMapper: ProductMapper
  ) { }

  findAllProducts(): Observable<Product[] | Error> {
    return new Observable<Product[] | Error>((subscriber) => {
      this.httpClient.get<any>(this.BASE_ROUTE)
        .subscribe(
          (responseBody) => {
            // Map the products to its correspondent model
            const products = this.productMapper.asProducts(responseBody.content);
            subscriber.next(products);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            // If the response body has content, then the server has answered (otherwise could not connect to server)
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

}
