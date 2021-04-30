import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {OrderService} from '../../services/order.service';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user.model';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public userDisplay: User = new User();

  public password1 = '';
  public password2 = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService
  ) {
    this.route.queryParams.subscribe(params => {
      let userId = params.id;
      if (userId == null) {
        userId = localStorage.getItem('user_id');
        if (userId == null) {
          this.router.navigate(['/login']).then();
          return;
        }
      }
      userId = Number(userId);
      const observable = userService.findUser(userId);
      observable.subscribe((response) => {
        if (response instanceof Error) {
          this.router.navigate(['/error']).then();
        } else {
          this.userDisplay = response;
          console.log(response);
        }
      });
    });
  }

  ngOnInit(): void {
  }


}
