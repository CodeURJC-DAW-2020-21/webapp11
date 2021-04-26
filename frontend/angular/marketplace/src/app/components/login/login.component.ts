import { Component, OnInit } from '@angular/core';
import {LoginService} from '../../services/login.service';
import {AuthResponse} from '../../models/auth-response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public email: string;
  public password: string;
  public message: string;
  public state: string;

  constructor(private loginService: LoginService) {
    this.email = '';
    this.password = '';
    this.message = '';
  }

  ngOnInit(): void {
  }

  logIn(): void {
    const observable = this.loginService.logIn(this.email, this.password);
    observable.subscribe((response) => {
      const status = response.statusCode;
      if (status === 200) {
        this.state = 'SUCCESS';
        this.message = 'You have been successfully logged in! Redirecting...';
      } else if (status === 400) {
        this.state = 'ERROR';
        this.message = 'The provided credentials are missing or invalid!';
      } else {
        this.state = 'DOWN';
        this.message = 'Could not connect to the server. Is the server down?';
      }
    });
  }

}
