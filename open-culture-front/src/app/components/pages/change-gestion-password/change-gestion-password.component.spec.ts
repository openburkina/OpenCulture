import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeGestionPasswordComponent } from './change-gestion-password.component';

describe('ChangeGestionPasswordComponent', () => {
  let component: ChangeGestionPasswordComponent;
  let fixture: ComponentFixture<ChangeGestionPasswordComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChangeGestionPasswordComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeGestionPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
