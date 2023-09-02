import { TestBed } from '@angular/core/testing';

import { MovieFavouritesService } from './movie-favourites.service';

describe('MovieFavouritesService', () => {
  let service: MovieFavouritesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovieFavouritesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
