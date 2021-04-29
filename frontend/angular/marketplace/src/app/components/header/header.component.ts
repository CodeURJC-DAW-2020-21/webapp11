import { Component, OnInit } from '@angular/core';
import {TokenService} from '../../services/token.service';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user.model';
import {Error} from '../../models/error.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  public errorMessage = '';
  public loggedUser: User = new User();

  ngOnInit(): void {
    const userIsPresent = localStorage.getItem('user_id') !== null;
    if (!userIsPresent) { return; }
    const userId = Number(localStorage.getItem('user_id'));
    const observable = this.userService.findUser(userId);
    observable.subscribe((response) => {
      if (response instanceof Error) {
        this.errorMessage = response.message;
      } else {
        this.loggedUser = response;
      }
    });
  }

  logOut(): void {
    localStorage.clear();
    this.loggedUser = new User();
    this.router.navigate(['/home']).then();
  }

}
