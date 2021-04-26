import { Injectable } from '@angular/core';
import {Product} from '../models/product.model';
import {AuthResponse} from '../models/auth-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthResponseMapper {

  constructor() { }

  asAuthResponse(response: any): AuthResponse {
    return new AuthResponse(
      response.status_name,
      response.status_code,
      response.content
    );
  }

}
