import { TestBed } from '@angular/core/testing';

import { NewTicketGuard } from './new-ticket.guard';

describe('NewTicketGuardGuard', () => {
  let guard: NewTicketGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(NewTicketGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
