import { TestBed } from '@angular/core/testing';
import { EcoAnalyzer } from './eco-analyzer';

describe('EcoAnalyzer', () => {
  let service: EcoAnalyzer;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EcoAnalyzer);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
