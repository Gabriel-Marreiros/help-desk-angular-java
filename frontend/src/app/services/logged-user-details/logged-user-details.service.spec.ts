import { TestBed } from '@angular/core/testing';

import { LoggedUserDetailsService } from './logged-user-details.service';

describe('LoggedUserDetailsService', () => {
  let service: LoggedUserDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoggedUserDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
