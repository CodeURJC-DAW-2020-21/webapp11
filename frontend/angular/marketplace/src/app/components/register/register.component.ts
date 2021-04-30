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
export class RegisterComponent implements OnInit {

  public error = '';

  public user: User = new User();

  public password1 = '';
  public password2 = '';

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  register(): void {
    if (!this.checkValidInputs()) { return; }
    const observable = this.userService.registerUser(this.user);
    observable.subscribe((response) => {
      if (response instanceof Error) {
        this.error = response.message;
      } else {
        this.user = response;
        this.router.navigate(['/login']).then();
      }
    });
  }

  checkValidInputs(): boolean {
    if (!/^[a-zA-Z -]+$/.test(this.user.firstName)) {
      this.error = 'The first name must be alphabetic';
      return false;
    }
    if (!/^[a-zA-Z -]+$/.test(this.user.surname)) {
      this.error = 'The surname must be alphabetic';
      return false;
    }
    if (!/\S+@\S+(\.\S+)?/.test(this.user.email)) {
      this.error = 'Please introduce a valid email address';
      return false;
    }
    if (this.user.address === '') {
      this.error = 'Address can\'t be empty';
      return false;
    }
    if (this.password1 !== this.password2) {
      this.error = 'The two password do not match';
      return false;
    }
    if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.password1)) {
      this.error = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
      return false;
    }
    if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.password2)) {
      this.error = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
      return false;
    }
    this.error = '';
    return true;
  }

}

