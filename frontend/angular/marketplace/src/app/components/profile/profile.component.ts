import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {User} from '../../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  public user: User = new User();

  public errorMessage = '';
  public displayName = '';

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
          this.displayName = `${user.firstName} ${user.surname}`;
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
    if (!this.checkValidInputs()) { return; }
    this.user.encodedImage = this.base64Image;
    const observable = this.userService.saveUser(this.user);
    observable.subscribe(
      (user: User) => {
        this.user = user;
        this.displayName = `${user.firstName} ${user.surname}`;
        window.location.reload();
      },
      (error) => {
        this.router.navigate(['/error']).then();
      }
    );
  }

  checkValidInputs(): boolean {
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
    if (this.initialPassword !== '') {
      if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.initialPassword)) {
        this.errorMessage = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
        return false;
      }
      if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(this.confirmPassword)) {
        this.errorMessage = 'Password must contain at least one digit, one uppercase and must be 8 characters long minimum';
        return false;
      }
    }
    return true;
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
