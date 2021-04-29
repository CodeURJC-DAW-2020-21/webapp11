import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { TokenService } from './token.service';
import {AuthResponseMapper} from '../mappers/auth.mapper';

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
  logIn(email: string, password: string): Observable<AuthResponse> {
    return new Observable((subscriber) => {
      this.httpClient.post<any>(this.BASE_ROUTE, { email, password })
        .subscribe(
          (responseBody) => {
            const authResponse = this.authResponseMapper.asAuthResponse(responseBody);
            // Store the provided token
            this.tokenService.saveToken(authResponse.content.token);
            // Store the user id
            localStorage.setItem('user_id', responseBody.content.user_id);
            subscriber.next(authResponse);
          },
          (errorResponse) => {
            const responseBody = errorResponse.error;
            // If the response body has content, then the server has answered (otherwise could not connect to server)
            const error = 'content' in responseBody ? this.authResponseMapper.asAuthResponse(responseBody) : new AuthResponse();
            subscriber.next(error);
          }
        );
    });
  }

}
