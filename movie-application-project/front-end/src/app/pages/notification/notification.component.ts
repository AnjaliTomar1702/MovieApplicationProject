import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../../service/notification.service';
import { Notification } from '../../service/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  constructor(private notificationService:NotificationService){}

  ngOnInit(): void {
    this.notificationService.getNotifications().subscribe(res => this.dataSource = res);
  }

  displayedColumns: string[] = ['position', 'movieName', 'releaseDate', 'button'];
  dataSource: Notification[] = [{
    position: 1,
    movieName: '123',
    releaseDate: '123'
  }];
}