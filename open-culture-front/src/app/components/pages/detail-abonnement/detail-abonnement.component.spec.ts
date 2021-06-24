import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailAbonnementComponent } from './detail-abonnement.component';

describe('DetailAbonnementComponent', () => {
  let component: DetailAbonnementComponent;
  let fixture: ComponentFixture<DetailAbonnementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailAbonnementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailAbonnementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
