import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, Subscriber} from 'rxjs';
import {Error} from '../models/error.model';
import {TokenService} from './token.service';
import {Order} from '../models/order.model';
import {OrderMapper} from '../mappers/order.mapper';
import {Constants} from '../configs/constants';
import * as fileSaver from 'file-saver';

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

  placeOrder(productId: number): Observable<boolean> {
    return new Observable<boolean>((subscriber: Subscriber<boolean>) => {
      const requestBody = { product_id: productId };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.post<any>(this.BASE_ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            subscriber.next(true);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  renewOrder(orderId: number): Observable<Order> {
    const ROUTE = this.BASE_ROUTE + '/' + orderId;
    return new Observable<Order>((subscriber: Subscriber<Order>) => {
      const requestBody = { months: 1 };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const order = this.orderMapper.asOrder(responseBody.content);
            subscriber.next(order);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  cancelOrder(orderId: number): Observable<Order> {
    const ROUTE = this.BASE_ROUTE + '/' + orderId;
    return new Observable<Order>((subscriber: Subscriber<Order>) => {
      const requestBody = { cancel: true };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const order = this.orderMapper.asOrder(responseBody.content);
            subscriber.next(order);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
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
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  findOrder(orderId: number): Observable<Order> {
    const ROUTE = `${this.BASE_ROUTE}/${orderId}`;
    return new Observable<Order>((subscriber: Subscriber<Order>) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const order = this.orderMapper.asOrder(responseBody.content);
            subscriber.next(order);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  downloadPdfOrder(orderId: number): void {
    const ROUTE = `${this.BASE_ROUTE}/${orderId}?content=pdf`;
    const FILE_NAME = `order_${orderId}.pdf`;
    const requestOptions: any = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }), responseType: 'blob' };
    this.httpClient.get<any>(ROUTE, requestOptions).subscribe(blob => { fileSaver.saveAs(blob as unknown as string, FILE_NAME); });
  }

}
