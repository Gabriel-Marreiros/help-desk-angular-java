import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileButtonComponent } from './user-profile-button.component';

describe('UserProfileButtonComponent', () => {
  let component: UserProfileButtonComponent;
  let fixture: ComponentFixture<UserProfileButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserProfileButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfileButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
