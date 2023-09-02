import { TestBed } from '@angular/core/testing';

import { MovieTrailerService } from './movie-trailer.service';

describe('MovieTrailerService', () => {
  let service: MovieTrailerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovieTrailerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
