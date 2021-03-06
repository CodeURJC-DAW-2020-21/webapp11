import { Injectable } from '@angular/core';
import {TokenService} from './token.service';
import {Observable, Subscriber, throwError} from 'rxjs';
import {Product} from '../models/product.model';
import { Error } from '../models/error.model';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ProductMapper} from '../mappers/product.mapper';
import {User} from '../models/user.model';
import {UserMapper} from '../mappers/user.mapper';
import {Constants} from '../configs/constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/users`;

  constructor(
    private httpClient: HttpClient,
    private tokenService: TokenService,
    private userMapper: UserMapper
  ) { }

  findUser(userId: number): Observable<User> {
    const ROUTE = `${this.BASE_ROUTE}/${userId}`;
    return new Observable<User>((subscriber: Subscriber<User>) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const user = this.userMapper.asUser(responseBody.content);
            subscriber.next(user);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  findUsers(page: number, amount: number): Observable<User[] | Error> {
    const ROUTE = `${this.BASE_ROUTE}?page=${page}&amount=${amount}`;
    return new Observable<User[] | Error>((subscriber) => {
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.get<any>(ROUTE, requestOptions)
        .subscribe(
          (responseBody) => {
            const users = this.userMapper.asUsers(responseBody.content);
            subscriber.next(users);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  enableUser(userId: number): Observable<User | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${userId}`;
    return new Observable<User | Error>((subscriber) => {
      const requestBody = { enabled: true };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const user = this.userMapper.asUser(responseBody.content);
            subscriber.next(user);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  disableUser(userId: number): Observable<User | Error> {
    const ROUTE = `${this.BASE_ROUTE}/${userId}`;
    return new Observable<User | Error>((subscriber) => {
      const requestBody = { enabled: false };
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const user = this.userMapper.asUser(responseBody.content);
            subscriber.next(user);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            const error = 'content' in responseBody ? Error.answered(responseBody.content) : Error.unanswered();
            subscriber.next(error);
          }
        );
    });
  }

  saveUser(user: User): Observable<User> {
    const ROUTE = `${this.BASE_ROUTE}/${user.id}`;
    return new Observable<User>((subscriber: Subscriber<User>) => {
      const requestBody = this.userMapper.asSaveRequest(user);
      console.log(requestBody);
      const requestOptions = { headers: new HttpHeaders({ Authorization: this.tokenService.getToken() }) };
      this.httpClient.put<any>(ROUTE, requestBody, requestOptions)
        .subscribe(
          (responseBody) => {
            const requestUser = this.userMapper.asUser(responseBody.content);
            subscriber.next(requestUser);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  registerUser(registerUser: User): Observable<boolean> {
    return new Observable<boolean>((subscriber: Subscriber<boolean>) => {
      const registerRequest = this.userMapper.asRegisterRequest(registerUser);
      this.httpClient.post<any>(this.BASE_ROUTE, registerRequest)
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

}
