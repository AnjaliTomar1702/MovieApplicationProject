import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Movie } from '../model/movie';
@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl: string = 'http://34.83.1.21';
  constructor(private http: HttpClient) {
  }
  public getNotifications(): Observable<Notification[]> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }
    return this.http.get<Movie[]>(this.baseUrl + '/api/v1/user/notification', httpOptions).
      pipe(map(i => {
        let notificationArray = new Array<Notification>();
        for (let j = 0; j < i.length; j++) {
          let notification = new Notification();
          notification.position = j + 1;
          notification.movieName = i[j].originalTitle;
          notification.releaseDate = i[j].releaseDate;
          notificationArray.push(notification);
        }
        return notificationArray;
      }));
  }


  public addNotification(movie: Movie): Observable<any> {
    console.log(movie);
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }
    return this.http.post<Boolean>(this.baseUrl + '/api/v1/user/notification', movie, httpOptions);
  }
}
export class Notification {
  position: number = 0;
  movieName: string = "";
  releaseDate: string = "";
}
