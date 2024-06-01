import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TechnicalsListComponent } from './technicals-list.component';

describe('TechnicalsListComponent', () => {
  let component: TechnicalsListComponent;
  let fixture: ComponentFixture<TechnicalsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TechnicalsListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TechnicalsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
