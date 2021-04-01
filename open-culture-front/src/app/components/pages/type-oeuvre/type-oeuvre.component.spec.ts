import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeOeuvreComponent } from './type-oeuvre.component';

describe('TypeOeuvreComponent', () => {
  let component: TypeOeuvreComponent;
  let fixture: ComponentFixture<TypeOeuvreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TypeOeuvreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeOeuvreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
