import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { TokenService } from './token.service';

@Injectable({ providedIn: 'root' })
export class LoginService {

  static readonly ROUTE: string = 'https://localhost:8443/api/tokens';

  constructor(private http: HttpClient, private tokenService: TokenService) {
  }

  /**
   * Returns an observable result of the authentication response {@link AuthResponse}.
   * @param email the email of the user that is to be logged in
   * @param password the password of the user that is to be logged in
   */
  logIn(email: string, password: string): Observable<AuthResponse> {
    return new Observable((subscriber) => {
      this.http.post<any>(LoginService.ROUTE, {email, password})
        .subscribe(
          (data) => {
            const response = new AuthResponse(data.status_name, data.status_code, data.content);
            this.tokenService.saveToken(data.content.token);
            subscriber.next(response);
          },
          (error) => {
            const json = error.error;
            if (error.content == null) {
              // If the server did not respond
              const response = new AuthResponse();
              subscriber.next(response);
            } else {
              // If the server sent an error response
              const response = new AuthResponse(json.status_name, json.status_code, json.content);
              subscriber.next(response);
            }
          }
        );
    });
  }

}
