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

  asSaveRequest(user: User): any {
    let contentType = '';
    let encodedImage = '';
    if (user.encodedImage !== '') {
      const firstSplit = user.encodedImage.split(';');
      contentType = firstSplit[0].split(':')[1];
      const secondSplit = firstSplit[1].split(',');
      encodedImage = secondSplit[1];
    }
    return {
      enabled: true,
      first_name: user.firstName,
      surname: user.surname,
      address: user.address,
      email: user.email,
      password: user.password,
      content_type: contentType,
      encoded_picture: encodedImage
    };
  }

}
