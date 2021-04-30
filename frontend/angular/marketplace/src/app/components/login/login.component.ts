import { Component, OnInit } from '@angular/core';
import {LoginService} from '../../services/login.service';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  // Bound properties
  public email: string;
  public password: string;

  // Interface variables
  public message: string;
  public state: string;

  // Messages
  private SUCCESS_MESSAGE = 'You have been successfully logged in! Redirecting...';
  private ERROR_MESSAGE = 'The provided credentials are missing or invalid!';
  private DOWN_MESSAGE = 'Could not connect to the server. Is the server down?';
  private INVALID_EMAIL_MESSAGE = 'Please introduce a valid email';

  constructor(private loginService: LoginService) {
    this.email = '';
    this.password = '';
    this.message = '';
  }

  logIn(): void {
    if (!this.isValidEmail(this.email)) {
      this.displayMessage('ERROR', this.INVALID_EMAIL_MESSAGE);
      return;
    }
    const observable = this.loginService.logIn(this.email, this.password);
    observable.subscribe(
      () => {
        this.displayMessage('SUCCESS', this.SUCCESS_MESSAGE);
        window.location.replace('/home');
      },
      (error: Error) => {
        if (error.isBadRequest()) {
          this.displayMessage('ERROR', this.ERROR_MESSAGE);
        } else {
          this.displayMessage('DOWN', this.DOWN_MESSAGE);
        }
      }
    );
  }

  isValidEmail(email: string): boolean {
    return /\S+@\S+(\.\S+)?/.test(email);
  }

  displayMessage(type: string, message: string): void {
    this.state = type;
    this.message = message;
  }

}
