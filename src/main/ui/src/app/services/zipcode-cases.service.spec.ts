import { TestBed } from '@angular/core/testing';

import { ZipcodeCasesService } from './zipcode-cases.service';

describe('ZipcodeCasesService', () => {
  let service: ZipcodeCasesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ZipcodeCasesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
