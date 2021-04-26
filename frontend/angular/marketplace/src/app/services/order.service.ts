import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Error} from '../models/error.model';
import {TokenService} from './token.service';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private BASE_ROUTE = 'https://localhost:8443/api/orders';

  constructor(
    private httpClient: HttpClient,
    private tokenService: TokenService
  ) { }

  /**
   * Places an order for given product id.
   * @param productId the identifier correspondent to the product that is about to be bought
   * @return observable an observable result of a boolean representing the correct order placement, an error otherwise
   */
  placeOrder(productId: number): Observable<boolean | Error> {
    return new Observable<boolean | Error>((subscriber) => {
      const requestBody = { product_id: productId };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.post<any>(
        this.BASE_ROUTE, requestBody, requestOptions)
        .subscribe(
          () => {
            subscriber.next(true);
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
