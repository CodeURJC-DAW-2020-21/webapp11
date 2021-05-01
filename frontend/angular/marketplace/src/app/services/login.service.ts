import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subscriber} from 'rxjs';
import {TokenService} from './token.service';
import {AuthResponseMapper} from '../mappers/auth.mapper';
import {Error} from '../models/error.model';
import {Constants} from '../configs/constants';

@Injectable({providedIn: 'root'})
export class LoginService {

  private BASE_ROUTE = `${Constants.BASE_URL}/api/tokens`;

  constructor(
    private httpClient: HttpClient,
    private authResponseMapper: AuthResponseMapper,
    private tokenService: TokenService
  ) { }

  logIn(email: string, password: string): Observable<boolean> {
    return new Observable((subscriber: Subscriber<boolean>) => {
      this.httpClient.post<any>(this.BASE_ROUTE, {email, password})
        .subscribe(
          (responseBody) => {
            const authResponse = this.authResponseMapper.asAuthResponse(responseBody);
            this.tokenService.saveToken(authResponse.token);
            this.storeUserId(authResponse.userId);
            subscriber.next(true);
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  storeUserId(userId: number): void {
    const key = 'user_id';
    const value = String(userId);
    localStorage.setItem(key, value);
  }

}
