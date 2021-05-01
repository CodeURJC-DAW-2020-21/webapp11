import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Error} from '../models/error.model';
import {TokenService} from './token.service';
import {Order} from '../models/order.model';
import {OrderMapper} from '../mappers/order.mapper';
import {Constants} from '../configs/constants';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/orders`;

  constructor(
    private httpClient: HttpClient,
    private orderMapper: OrderMapper,
    private tokenService: TokenService
  ) { }

  placeOrder(productId: number): Observable<boolean | Error> {
    return new Observable<boolean | Error>((subscriber) => {
      const requestBody = { product_id: productId };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.post<any>(this.BASE_ROUTE, requestBody, requestOptions)
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

  renewOrder(orderId: number): Observable<Order | Error> {
    const ROUTE = this.BASE_ROUTE + '/' + orderId;
    return new Observable<Order | Error>((subscriber) => {
      const requestBody = { months: 1 };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const order = this.orderMapper.asOrder(responseBody.content);
            subscriber.next(order);
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

  findOrders(page: number, amount: number): Observable<Order[] | Error> {
    const ROUTE = `${this.BASE_ROUTE}?page=${page}&amount=${amount}`;
    return new Observable<Order[] | Error>((subscriber) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const orders = this.orderMapper.asOrders(responseBody.content);
            subscriber.next(orders);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            // If the response body has content, then the server has answered (otherwise could not connect to server)
            const error = responseBody != null ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  findOrder(orderId: number): Observable<Order | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${orderId}`;
    return new Observable<Order | Error>((subscriber) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const order = this.orderMapper.asOrder(responseBody.content);
            subscriber.next(order);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            // If the response body has content, then the server has answered (otherwise could not connect to server)
            const error = responseBody != null ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

}
