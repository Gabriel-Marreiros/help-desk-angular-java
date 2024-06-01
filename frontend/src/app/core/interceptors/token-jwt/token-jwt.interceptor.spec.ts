import { TestBed } from '@angular/core/testing';

import { TokenJwtInterceptor } from './token-jwt.interceptor';

describe('TokenJwtInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      TokenJwtInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: TokenJwtInterceptor = TestBed.inject(TokenJwtInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
