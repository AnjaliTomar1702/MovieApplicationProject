import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { Movie } from 'src/app/model/movie';
import { MovieApiServiceService } from 'src/app/service/movie-api-service.service';
import { NotificationService } from 'src/app/service/notification.service';
@Component({
  selector: 'app-recommended',
  templateUrl: './recommended.component.html',
  styleUrls: ['./recommended.component.css']
})
export class RecommendedComponent implements OnInit {

  constructor(private movieService: MovieApiServiceService, private notificationService: NotificationService,
    private router: Router) { }

 Object = Object;
  recommendedMoviesResult: Movie[] = [];
  upcomingMoviesResult: Movie[] = [];
  recommendedMovieCards: any;
  actionMoviesResult: Movie[] = [];
  comedyMoviesResult: Movie[] = [];
  crimeMoviesResult: Movie[] = [];
  familyMoviesResult: Movie[] = [];
  genres:Movie[] = [];
  

  ngOnInit(): void {
    this.recommendedMovie();
    this.upcomingMovies();
    this.getRecommendedMovieCards();
  
  }

  recommendedMovie() {
    this.movieService.recommendedMovies().subscribe((result) => {
      this.recommendedMoviesResult = result;
   
    });
  }
  upcomingMovies() {
    this.movieService.upComingMovies().subscribe((result) => {
      this.upcomingMoviesResult = result;
    });
  }
  getRecommendedMovieCards() {
    this.movieService.recommendedMoviesData().subscribe((result) => {
      this.recommendedMovieCards = result;
           
    });
  }
  

}
