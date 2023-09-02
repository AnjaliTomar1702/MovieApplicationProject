import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Movie } from '../model/movie';
import { Trailer } from '../model/trailer';
import { Cast } from '../model/cast';


@Injectable({
  providedIn: 'root'
})
export class MovieTrailerService {
 

  private movieDetailsUrl: string =  "http://34.83.1.21/api/v1/recommended/movie/searchById/";

  constructor(private http: HttpClient) { }

  public getTrailorsId(id: string): Observable<any> {  
    return this.http.get<any>(`${this.movieDetailsUrl}/${id}/trailer`);

  }

  getMovie(id: String): Observable<Movie> {
    return this.http.get<Movie>(this.movieDetailsUrl + id);
  }


  public getMovieCast(id: string): Observable<Cast[]> {
   
    return this.http.get<any>(`${this.movieDetailsUrl}/${id}/cast`);
  }
  public getMovieReviews(id: string): Observable<any> {
   
    return this.http.get<any>(`${this.movieDetailsUrl}/${id}/review`);
  }
}
