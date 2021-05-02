import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Error} from '../models/error.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';
import {SaleMapper} from '../mappers/sale.mapper';
import {Sale} from '../models/sale.model';
import {Constants} from '../configs/constants';

@Injectable({
  providedIn: 'root'
})
export class SaleService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/sales`;

  constructor(
    private httpClient: HttpClient,
    private saleMapper: SaleMapper,
    private tokenService: TokenService,
  ) { }

  findSale(saleType: string): Observable<Sale | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${saleType}`;
    return new Observable<Sale | Error>((subscriber) => {
      this.httpClient.get<any>(ROUTE)
        .subscribe(
          (responseBody) => {
            const sale = this.saleMapper.asSale(responseBody.content);
            subscriber.next(sale);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
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
            const error = Error.from(errorResponse);
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
            const error = Error.from(errorResponse);
            subscriber.next(error);
          }
        );
    });
  }

}
