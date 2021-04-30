import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { TokenService } from './token.service';
import {AuthResponseMapper} from '../mappers/auth.mapper';
import {Error} from '../models/error.model';

@Injectable({ providedIn: 'root' })
export class LoginService {

  private BASE_ROUTE = 'https://localhost:8443/api/tokens';

  constructor(
    private httpClient: HttpClient,
    private authResponseMapper: AuthResponseMapper,
    private tokenService: TokenService) { }

  /**
   * Returns an observable result of {@link AuthResponse}.
   * @param email the email credential
   * @param password the password credential
   * @return an observable result of {@link AuthResponse}
   */
  logIn(email: string, password: string): Observable<void> {
    return new Observable((subscriber) => {
      this.httpClient.post<any>(this.BASE_ROUTE, { email, password })
        .subscribe(
          (responseBody) => {
            const authResponse = this.authResponseMapper.asAuthResponse(responseBody);
            this.tokenService.saveToken(authResponse.token);
            localStorage.setItem('user_id', String(authResponse.userId));
            subscriber.next();
          },
          (errorResponse) => {
            const error = this.createError(errorResponse);
            subscriber.error(error);
          }
        );
    });
  }

  // TODO: Move to a separate factory class
  createError(errorResponse: any): Error {
    if (errorResponse.status === 400) { return Error.badRequest(); }
    if (errorResponse.status === 401) { return Error.unauthorized(); }
    if (errorResponse.status === 404) { return Error.notFound(); }
    if (errorResponse.status !== 0) { return Error.answered(errorResponse.error.content); }
    return Error.unanswered();
  }

}
