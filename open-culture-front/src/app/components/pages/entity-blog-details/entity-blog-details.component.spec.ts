import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityBlogDetailsComponent } from './entity-blog-details.component';

describe('EntityBlogDetailsComponent', () => {
  let component: EntityBlogDetailsComponent;
  let fixture: ComponentFixture<EntityBlogDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityBlogDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityBlogDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
