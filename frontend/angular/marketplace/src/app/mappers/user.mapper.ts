import { Injectable } from '@angular/core';
import {Product} from '../models/product.model';
import {User} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserMapper {

  constructor() { }

  asUsers(response: any): User[] {
    return response.map((item: any) => this.asUser(item));
  }

  asUser(item: any): User {
    return new User(
      item.id,
      item.first_name,
      item.surname,
      item.address,
      item.email,
      item.enabled,
      item.encoded_image,
      item.is_admin,
      ''
    );
  }

  asRegisterRequest(user: User): any {
    return {
      first_name: user.firstName,
      surname: user.surname,
      address: user.address,
      email: user.email,
      password: user.password
    };
  }

}
