import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OeuvreEditComponent } from './oeuvre-edit.component';

describe('OeuvreEditComponent', () => {
  let component: OeuvreEditComponent;
  let fixture: ComponentFixture<OeuvreEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OeuvreEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OeuvreEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
