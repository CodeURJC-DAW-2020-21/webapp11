import { Component, OnInit } from '@angular/core';
import {OrderService} from '../../services/order.service';
import {Error} from '../../models/error.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Order} from '../../models/order.model';

@Component({
  selector: 'app-service',
  templateUrl: './service.component.html',
  styleUrls: ['./service.component.css']
})
export class ServiceComponent implements OnInit {

  public cpuColor = 'bg-success';
  public ramColor = 'bg-success';
  public animationEnabled = true;
  public restartingServer = false;
  public serverStarted = true;

  public cpuNow = 0;
  public ramNow = 0;

  public errorMessage = '';

  public order: Order = new Order();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private orderService: OrderService
  ) {
    this.route.queryParams.subscribe(params => {
      let orderId = params.id;
      if (orderId == null) { return; }
      orderId = Number(orderId);
      this.loadOrder(orderId);
    });
  }

  ngOnInit(): void {
  }

  loadOrder(orderId: number): void {
    const observable = this.orderService.findOrder(orderId);
    observable.subscribe(
      (order: Order) => {
        this.order = order;
        this.runAnimation();
      },
      (error: Error) => {
        this.router.navigate(['/error']).then();
      }
    );
  }

  renewOrder(): void {
    if (this.order.orderId === -1) { return; }
    const observable = this.orderService.renewOrder(this.order.orderId);
    observable.subscribe(
      (order: Order) => {
        this.order = order;
      },
      (error: Error) => {
        this.router.navigate(['/error']).then();
      }
    );
  }

  cancelOrder(): void {
    if (this.order.orderId === -1) { return; }
    const observable = this.orderService.cancelOrder(this.order.orderId);
    observable.subscribe(
      (order: Order) => {
        this.order = order;
        this.stopAnimation();
      },
      (error: Error) => {
        this.router.navigate(['/error']).then();
      }
    );
  }

  downloadPdfOrder(): void {
    if (this.order.orderId === -1) { return; }
    this.orderService.downloadPdfOrder(this.order.orderId);
  }

  runAnimation(): void {
    if (this.order.isExpired) {
      this.serverStarted = false;
      return;
    }
    this.cpuNow = 75;
    this.ramNow = 50;
    setInterval(() => {
      if (!this.animationEnabled) { return; }
      const newCpu = this.generateRandomInRange(-10, 10);
      const newRam = this.generateRandomInRange(-10, 10);
      this.cpuNow += newCpu;
      this.ramNow += newRam;
      if (this.cpuNow < 25) { this.cpuNow = 25; }
      if (this.cpuNow >= 100) { this.cpuNow = 80; }
      if (this.ramNow < 25) { this.ramNow = 25; }
      if (this.ramNow >= 100) { this.ramNow = 80; }
      if (this.cpuNow > 50 && this.cpuNow < 75) { this.cpuColor = 'bg-warning'; }
      if (this.cpuNow <= 50) { this.cpuColor = 'bg-success'; }
      if (this.cpuNow >= 75) { this.cpuColor = 'bg-danger'; }
      if (this.ramNow > 50 && this.ramNow < 75) { this.ramColor = 'bg-warning'; }
      if (this.ramNow <= 50) { this.ramColor = 'bg-success'; }
      if (this.ramNow >= 75) { this.ramColor = 'bg-danger'; }
    }, 1000);
  }

  stopAnimation(): void {
    this.animationEnabled = false;
    this.serverStarted = false;
    this.cpuNow = 0;
    this.ramNow = 0;
    this.cpuColor = 'bg-success';
    this.ramColor = 'bg-success';
  }

  restartServer(): void {
    this.animationEnabled = false;
    this.restartingServer = true;
    this.cpuNow = 0;
    this.ramNow = 0;
    this.cpuColor = 'bg-success';
    this.ramColor = 'bg-success';
    setTimeout(() => {
      this.cpuNow = 50;
      this.ramNow = 85;
      this.cpuColor = 'bg-warning';
      this.ramColor = 'bg-danger';
      this.restartingServer = false;
      this.animationEnabled = true;
    }, 3500);
  }

  startAnimation(): void {
    this.cpuNow = 50;
    this.ramNow = 85;
    this.cpuColor = 'bg-warning';
    this.ramColor = 'bg-danger';
    this.animationEnabled = true;
    this.serverStarted = true;
  }

  generateRandomInRange(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min) + min);
  }

}
