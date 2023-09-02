import { Component,HostListener, Input, ViewChild } from '@angular/core';
import { MatLegacyMenuTrigger as MatMenuTrigger } from '@angular/material/legacy-menu';
import { MovieFavouritesService } from './service/movie-favourites.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'movie-app';
 
  sideNavStatus:boolean = false;
  sessionStorage = sessionStorage;
  constructor(private moviefavourites:MovieFavouritesService,    private router: Router
    ){
    if(!this.moviefavourites.favourites){
      if(sessionStorage.getItem('token')){
        this.moviefavourites.getFavouriteMovies().subscribe(res => this.moviefavourites.favourites = res);
      }
    }
 
  }

}
