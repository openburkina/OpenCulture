import { TestBed } from '@angular/core/testing';

import { AuthJWTService } from './auth-jwt.service';

describe('AuthJWTService', () => {
  let service: AuthJWTService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthJWTService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
