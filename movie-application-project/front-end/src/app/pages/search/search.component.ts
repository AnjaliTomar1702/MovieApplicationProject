import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Genre } from 'src/app/model/genre';
import { Movie } from 'src/app/model/movie';
import { MovieApiServiceService } from 'src/app/service/movie-api-service.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {


  Object = Object;
  selectedYear: number = 0;
  searchResult: Movie[] = [];
  searchStr: string = "";
  genre: any;
  genreList: Genre[] = [];
  genreResult: Movie[] = [];
  years: number[] = [];
  yearResult: Movie[] = [];
  selectedGenre: number = 0;


  constructor(private service: MovieApiServiceService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    this.getGenre();
    this.selectedYear = new Date().getFullYear();
    for (let year = this.selectedYear; year >= 2001; year--) {
      this.years.push(year);
    }
  }


  search() {
    this.service.searchMovie(this.searchStr).subscribe(result => {
      this.searchResult = result;
    });
  }
  searchByGenre() {
    this.service.searchMovieByGenre(this.searchStr).subscribe(result => {
      this.searchResult = result;
    });
  }

  searchByYear(event: any) {
    this.service.searchMovieByYear(event.target.value).subscribe(result => {
      this.yearResult = result;
    });
  }

  getGenre() {
    this.service.getGenreList().subscribe(result => {
      this.genreList = result;
    });
  }

  searchByGenreIdQuery(id: any) {
    this.service.searchByGenreList(id.target.value).subscribe(result => {
      this.genreResult = result;
    });
  }

}

