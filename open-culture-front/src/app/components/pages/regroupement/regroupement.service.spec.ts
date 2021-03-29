import { TestBed } from '@angular/core/testing';

import { RegroupementService } from './regroupement.service';

describe('RegroupementService', () => {
  let service: RegroupementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegroupementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
