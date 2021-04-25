import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  static readonly BEARER: string = 'Token ';

  constructor() { }

  /**
   * Given a token value without a bearer stores it.
   * @param token the token value to be stored
   */
  saveToken(token: string): void {
    localStorage.setItem('token', TokenService.BEARER + token);
  }

  /**
   * Checks if the client has any stored token.
   * @return boolean true if the client has a stored token, false otherwise
   */
  hasToken(): boolean {
    const token = localStorage.getItem('token');
    return token != null;
  }

  /**
   * Returns the client stored token.
   * @return token the token with the bearer if present, an empty string otherwise
   */
  getToken(): string {
    let token = localStorage.getItem('token');
    if (token == null) { token = ''; }
    return token;
  }

}
