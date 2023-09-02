import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MovieFavouritesService } from 'src/app/service/movie-favourites.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MovieTrailerService } from 'src/app/service/movie-trailer.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Cast } from 'src/app/model/cast';
import { Movie } from 'src/app/model/movie';

@Component({
  selector: 'app-pop-up',
  templateUrl: './pop-up.component.html',
  standalone: true,
})
export class popUpComponent {


  movie: any;
  movies !: Movie[];
  trailers: any;
  trailer!: string;
  casts !: Cast[];
  formGroup: any;

  constructor(private movieService: MovieTrailerService, private movieFavourites: MovieFavouritesService, public domSanitizer: DomSanitizer, private route: ActivatedRoute,
    private router: Router, public movieFavouritesService: MovieFavouritesService, public dialog: MatDialog, @Inject(MAT_DIALOG_DATA) public data: string
  ) { }

  ngOnInit(): void {
    this.trailer = this.data;
    // let originalTitle = this.route.snapshot.paramMap.get('originalTitle');
  }

  // getMovie(originalTitle: any) {
  //   originalTitle && this.movieService.getMovie(originalTitle).subscribe((data) => {
  //     this.movies = data;
  //     this.movie = data[0];
  //   })
  // }
  // getTrailor(originalTitle: any) {

  //   originalTitle && this.movieService.getTrailorsId(originalTitle).subscribe((result) => {
  //     console.log(result, 'getTrailer');
  //     this.trailers = result;
  //     this.trailer = this.trailers[0];
  //   });
  // }
}