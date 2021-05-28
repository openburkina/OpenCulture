import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OeuvreBlogDetailsComponent } from './oeuvre-blog-details.component';

describe('EntityBlogDetailsComponent', () => {
  let component: OeuvreBlogDetailsComponent;
  let fixture: ComponentFixture<OeuvreBlogDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OeuvreBlogDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OeuvreBlogDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
