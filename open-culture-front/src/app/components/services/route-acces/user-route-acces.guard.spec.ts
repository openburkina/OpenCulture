import { TestBed } from '@angular/core/testing';

import { UserRouteAccesGuard } from './user-route-acces.guard';

describe('UserRouteAccesGuard', () => {
  let guard: UserRouteAccesGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(UserRouteAccesGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
