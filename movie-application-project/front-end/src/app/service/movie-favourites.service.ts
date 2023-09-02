import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { Movie } from '../model/movie';

@Injectable({
  providedIn: 'root'
})
export class MovieFavouritesService {

  public favourites: Movie[] | null = null;
  public favouritesSubject:Subject<Movie[] | null> = new Subject();


  constructor(private http: HttpClient) {

  }


  baseUrl = 'http://34.83.1.21/api/v1/user/favourite'


  public getFavouriteMovies(): Observable<Movie[]> {
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }
    return this.http.get<Movie[]>(this.baseUrl, httpOptions);
  }



  addFavouriteMovies(movie: Movie) {
    console.log(movie, 'movie');
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }

    return this.http.post<Movie>(this.baseUrl, movie, httpOptions);

  }


  deleteFavouriteMovies(movieId: number) {
    let httpOptions = {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + sessionStorage.getItem('token')
      })
    }

    return this.http.delete<Movie>(this.baseUrl+'/'  + movieId, httpOptions);

  }
}
