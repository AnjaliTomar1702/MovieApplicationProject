import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DomSanitizer } from '@angular/platform-browser';
import { Movie } from 'src/app/model/movie';
import { Trailer } from 'src/app/model/trailer';
import { Cast } from 'src/app/model/cast';
import { MovieTrailerService } from 'src/app/service/movie-trailer.service';
import { MovieFavouritesService } from 'src/app/service/movie-favourites.service';
import { NotificationService } from 'src/app/service/notification.service';
import Swal from 'sweetalert2';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import { popUpComponent } from './pop-up.component';
import { Review } from 'src/app/model/review';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.css']
})
export class MovieDetailsComponent implements OnInit {

  movie: any;
  movieResult : any;
  trailers: any;
  trailer!: Trailer;
  casts !: Cast[];
  Date = Date;
  review:Review[]=[];
  

  constructor(private movieService: MovieTrailerService, private movieFavourites: MovieFavouritesService, public domSanitizer: DomSanitizer, private route: ActivatedRoute,
    private router: Router, public movieFavouritesService: MovieFavouritesService, private notificationService: NotificationService,public dialog: MatDialog
  ) {
    // this.movie = new Movie();
  }


  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    this.getMovie(id);
    this.getTrailor(id);
    this.getCast(id);
    this.getReview(id);
  }

  openDialog(key:string){
    let a: MatDialogConfig = new MatDialogConfig(); 
    
    a.data = key;
    this.dialog.open(popUpComponent,a);
}

  getMovie(id: any) {
    id && this.movieService.getMovie(id).subscribe((data) => {
      this.movie = data;
    })
  }

  getCast(id: any) {
    id && this.movieService.getMovieCast(id).subscribe((data) => {
      this.casts = data;
    })
  }

getReview(id:any){
  id && this.movieService.getMovieReviews(id).subscribe((data) => {
    console.log(data,'review');
    this.review = data;
  },
  error=>{
    this.review = error;
    console.log(error);
  })
}

  getTrailor(id: any) {
    id && this.movieService.getTrailorsId(id).subscribe((result) => {
      console.log(result,'trailer');
      this.trailers = result;
      this.trailer = this.trailers[0];
    });
  }

  add() {
    this.movieFavouritesService.addFavouriteMovies(this.movie).subscribe((data: any) => {
      Swal.fire({
        icon: 'success',
        title: 'Added Successfully!!!',
        color: '#dd3675',
        showConfirmButton: false,
        timer: 1500
      })
      this.movieFavouritesService.getFavouriteMovies().subscribe(res => {
        this.movieFavouritesService.favouritesSubject.next(res)
        // this.movieFavouritesService.favourites = res;
      }
      );

    })
  };

  delete(index: any) {
    this.movieFavourites.deleteFavouriteMovies(index).subscribe((result) => {
      Swal.fire({
        icon: 'success',
        title: 'Removed Successfully!!!',
        color: '#dd3675',
        showConfirmButton: false,
        timer: 1500
      })
      this.movieFavouritesService.getFavouriteMovies().subscribe(res => {
        this.movieFavouritesService.favouritesSubject.next(res)
        // this.movieFavouritesService.favourites = res;
      }

      );
    })
  }


  addNotification() {
    this.notificationService.addNotification(this.movie).subscribe((data: any) => {

      Swal.fire({
        icon: 'success',
        title: 'Notification Added!!!',
        color: '#dd3675',
        showConfirmButton: false,
        timer: 1500
      })
    })

  }
}

