import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityBlogComponent } from './entity-blog.component';

describe('EntityBlogComponent', () => {
  let component: EntityBlogComponent;
  let fixture: ComponentFixture<EntityBlogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityBlogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityBlogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
