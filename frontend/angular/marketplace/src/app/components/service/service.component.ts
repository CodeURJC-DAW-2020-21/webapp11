import { Component, OnInit } from '@angular/core';
import {OrderService} from '../../services/order.service';
import {Error} from '../../models/error.model';

@Component({
  selector: 'app-service',
  templateUrl: './service.component.html',
  styleUrls: ['./service.component.css']
})
export class ServiceComponent implements OnInit {

  public orderId = 41;
  public errorMessage = '';

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
  }

  renewService(): void {
    const observable = this.orderService.renewOrder(this.orderId);
    observable.subscribe((response) => {
      if (response instanceof Error) {
        // If there is an error show the error
        this.errorMessage = response.message;
      } else {
        // If renew order success -> refresh windows
        alert('I renewed the service successfully!');
        console.log(response.content);
      }
    });
  }

}
