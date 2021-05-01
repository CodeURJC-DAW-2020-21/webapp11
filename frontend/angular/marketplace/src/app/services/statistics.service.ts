import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Error} from '../models/error.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';
import {Statistics} from '../models/statistics.model';
import {StatisticsMapper} from '../mappers/statistics.mapper';
import {Constants} from '../configs/constants';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/statistics`;

  constructor(
    private httpClient: HttpClient,
    private statisticsMapper: StatisticsMapper,
    private tokenService: TokenService,
  ) { }

  findStatistics(): Observable<Statistics | Error> {
    return new Observable<Statistics | Error>((subscriber) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(this.BASE_ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const statistics = this.statisticsMapper.asStatistics(responseBody.content);
            subscriber.next(statistics);
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
