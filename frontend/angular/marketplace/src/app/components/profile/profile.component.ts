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

  public user: User = new User();

  public initialPassword = '';
  public confirmPassword = '';

  public base64Image = '';

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
      observable.subscribe(
        (user: User) => {
          user.id = userId;
          this.user = user;
        },
        (error) => {
          this.router.navigate(['/error']).then();
        }
      );
    });
  }

  ngOnInit(): void {
  }

  saveUser(): void {
    this.user.encodedImage = this.base64Image;
    const observable = this.userService.saveUser(this.user);
    observable.subscribe(
      (user: User) => {
        
      },
      (error) => {
        this.router.navigate(['/error']).then();
      }
    );
  }

  pictureUploadEvent(fileInput: any): void {
    if (fileInput.target.files && fileInput.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (event: any) => {
        const image = new Image();
        image.src = event.target.result;
        image.onload = () => { this.base64Image = event.target.result; };
      };
      reader.readAsDataURL(fileInput.target.files[0]);
    }
  }

}
