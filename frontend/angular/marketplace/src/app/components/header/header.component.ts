import { Component, OnInit } from '@angular/core';
import {TokenService} from '../../services/token.service';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public loggedUser: User = new User();

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    const userId = localStorage.getItem('user_id');
    const isUserLogged = userId != null;
    if (!isUserLogged) { return; }
    const observable = this.userService.findUser(Number(userId));
    observable.subscribe((user: User) => { this.loggedUser = user; });
  }

  logOut(): void {
    localStorage.clear();
    this.loggedUser = new User();
    this.router.navigate(['/home']).then();
  }

}
