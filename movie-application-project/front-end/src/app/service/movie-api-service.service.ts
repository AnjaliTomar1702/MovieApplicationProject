import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../model/movie';

@Injectable({
  providedIn: 'root'
})
export class MovieApiServiceService {


  private baseUrl: string = 'http://34.83.1.21';


  constructor(private http: HttpClient) { }

  upComingMovies(): Observable<Movie[]> {

    return this.http.get<Movie[]>(this.baseUrl + '/api/v1/recommended/movie/upcoming');
  }

  public recommendedMovies(): Observable<Movie[]> {

    return this.http.get<Movie[]>(this.baseUrl + '/api/v1/recommended/movie/popular');
  }

  public recommendedMoviesData(): Observable<Map<string, Movie[]>> {
    return this.http.get<Map<string, Movie[]>>(this.baseUrl + '/api/v1/recommended/movie/cards');
  }

  public searchMovie(movieName: string): Observable<any> {

    return this.http.get<any>(`${this.baseUrl + '/api/v1/recommended/movie/searchByName'}/${movieName}`);
  }
  public searchMovieByGenre(genre: string): Observable<any> {

    return this.http.get<any>(`${this.baseUrl + '/api/v1/recommended/movie/searchByGenreName'}/${genre}`);
  }

  public searchMovieByYear(year: string): Observable<any> {

    return this.http.get<any>(`${this.baseUrl + '/api/v1/recommended/movie/searchByYear'}/${year}`);
  }
  public getGenreList(): Observable<any> {

    return this.http.get<any>(`${this.baseUrl + '/api/v1/recommended/movie/genre'}`);
  }
  public searchByGenreList(genreId: string): Observable<any> {

    return this.http.get<any>(`${this.baseUrl + '/api/v1/recommended/movie/searchByGenreIdQuery'}/${genreId}`);
  }

  
}
