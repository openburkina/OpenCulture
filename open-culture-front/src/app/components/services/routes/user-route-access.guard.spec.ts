import { TestBed } from '@angular/core/testing';

import { UserRouteAccessGuard } from './user-route-access.guard';

describe('UserRouteAccessGuard', () => {
  let guard: UserRouteAccessGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(UserRouteAccessGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
