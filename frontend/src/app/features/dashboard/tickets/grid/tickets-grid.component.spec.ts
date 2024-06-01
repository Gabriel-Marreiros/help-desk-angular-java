import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TicketsGridComponent } from './tickets-grid.component';

describe('TicketsGridComponent', () => {
  let component: TicketsGridComponent;
  let fixture: ComponentFixture<TicketsGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TicketsGridComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TicketsGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
