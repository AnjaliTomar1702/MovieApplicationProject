import { Pipe, PipeTransform } from '@angular/core';
import { MovieFavouritesService } from '../service/movie-favourites.service';
import { Observable } from 'rxjs';
@Pipe({
  name: 'favAlreadyAdded'
})
export class FavAlreadyAddedPipe implements PipeTransform {
constructor(private favService:MovieFavouritesService){
}
  transform(value: number, ...args: unknown[]): Observable<boolean> {
    return new Observable<boolean>(observor => {
      let noInverse = args[0] as boolean;
      let present = false;
      if(this.favService.favourites == null){
        observor.next(noInverse);
      }
      if(this.favService.favourites){
        present = this.favService.favourites.filter(i => i.movieId == value).length > 0;
      }
      observor.next(noInverse ? present : !present);
      this.favService.favouritesSubject.subscribe(res => {
        if(res){
          console.log(res);
          let present2 = res.filter(i=>i.movieId==value).length > 0;
          observor.next(noInverse ? present2 : !present2);
        }
        else
          observor.next(!noInverse);
      })
    })
  }
}


