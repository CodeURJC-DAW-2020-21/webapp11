import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Error} from '../models/error.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';
import {Statistics} from '../models/statistics.model';
import {StatisticsMapper} from '../mappers/statistics.mapper';
import {SaleMapper} from '../mappers/sale.mapper';
import {Sale} from '../models/sale.model';

@Injectable({
  providedIn: 'root'
})
export class SaleService {

  private BASE_ROUTE = 'https://localhost:8443/api/sales';

  constructor(
    private httpClient: HttpClient,
    private saleMapper: SaleMapper,
    private tokenService: TokenService,
  ) { }

  findSale(saleType: string): Observable<Sale | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${saleType}`;
    return new Observable<Sale | Error>((subscriber) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const sale = this.saleMapper.asSale(responseBody.content);
            subscriber.next(sale);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  updateSale(saleType: string, saleToUpdate: Sale): Observable<Sale | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${saleType}`;
    return new Observable<Sale | Error>((subscriber) => {
      const requestBody = this.saleMapper.asUpdateRequest(saleToUpdate);
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const sale = this.saleMapper.asSale(responseBody.content);
            subscriber.next(sale);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  disableSale(saleType: string): Observable<boolean | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${saleType}`;
    return new Observable<boolean | Error>((subscriber) => {
      const requestBody = { enabled: false };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          () => {
            subscriber.next(true);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

}
