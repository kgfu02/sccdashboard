import { TestBed } from '@angular/core/testing';

import { KeyMetricService } from './key-metric.service';

describe('KeyMetricService', () => {
  let service: KeyMetricService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeyMetricService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
