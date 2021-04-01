import { TestBed } from '@angular/core/testing';

import { TypeOeuvreService } from './type-oeuvre.service';

describe('TypeOeuvreService', () => {
  let service: TypeOeuvreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeOeuvreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
