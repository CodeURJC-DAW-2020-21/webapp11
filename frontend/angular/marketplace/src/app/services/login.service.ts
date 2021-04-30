import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from './token.service';
import {AuthResponseMapper} from '../mappers/auth.mapper';
import {Error} from '../models/error.model';

@Injectable({providedIn: 'root'})
export class LoginService {

  private BASE_ROUTE = 'https://127.0.0.1:8443/api/tokens';

  constructor(
    private httpClient: HttpClient,
    private authResponseMapper: AuthResponseMapper,
    private tokenService: TokenService
  ) { }

  logIn(email: string, password: string): Observable<void> {
    return new Observable((subscriber) => {
      this.httpClient.post<any>(this.BASE_ROUTE, {email, password})
        .subscribe(
          (responseBody) => {
            const authResponse = this.authResponseMapper.asAuthResponse(responseBody);
            this.tokenService.saveToken(authResponse.token);
            localStorage.setItem('user_id', String(authResponse.userId));
            subscriber.next();
          },
          (errorResponse) => {
            const error = Error.from(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

}
