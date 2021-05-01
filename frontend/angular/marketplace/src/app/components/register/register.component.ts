import { Component, OnInit } from '@angular/core';
import {User} from '../../models/user.model';
import {UserService} from '../../services/user.service';
import {Error} from '../../models/error.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  public errorMessage = '';
  public successMessage = '';

  public user: User = new User();

  public initialPassword = '';
  public confirmPassword = '';

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  register(): void {
    if (!this.checkValidInputs()) { return; }
    this.user.password = this.initialPassword;
    const observable = this.userService.registerUser(this.user);
    observable.subscribe(
      (success: boolean) => {
        this.successMessage = `Welcome to DawHostServices! Redirecting...`;
        setTimeout(() => { this.router.navigate(['/login']).then(); }, 1500);
      },
      (error: Error) => {
        this.successMessage = '';
        if (error.isBadRequest()) { this.errorMessage = 'Maybe try a new email or password'; return; }
        this.errorMessage = 'Could not connect to the server.. Is it down?';
      }
    );
  }

  checkValidInputs(): boolean {
    this.successMessage = '';
    this.errorMessage = '';
    if (!/^[a-zA-Z -]+$/.test(this.user.firstName)) {
      this.errorMessage = 'The first name must be alphabetic';
      return false;
    }
    if (!/^[a-zA-Z -]+$/.test(this.user.surname)) {
      this.errorMessage = 'The surname must be alphabetic';
      return false;
    }
    if (!/\S+@\S+(\.\S+)?/.test(this.user.email)) {
      this.errorMessage = 'Please introduce a valid email address';
      return false;
    }
    if (this.user.address === '') {
      this.errorMessage = 'Address can\'t be empty';
      return false;
    }
    if (this.initialPassword !== this.confirmPassword) {
      this.errorMessage = 'The two passwords do not match';
      return false;
    }
    if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.initialPassword)) {
      this.errorMessage = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
      return false;
    }
    if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.confirmPassword)) {
      this.errorMessage = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
      return false;
    }
    this.successMessage = 'Registering a new account in process...';
    return true;
  }

}

