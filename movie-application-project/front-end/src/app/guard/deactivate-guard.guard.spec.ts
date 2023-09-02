import { TestBed } from '@angular/core/testing';

import { DeactivateGuardGuard } from './deactivate-guard.guard';

describe('DeactivateGuardGuard', () => {
  let guard: DeactivateGuardGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(DeactivateGuardGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
