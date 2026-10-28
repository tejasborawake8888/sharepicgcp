import { TestBed } from '@angular/core/testing';
import { PostLike } from './post-like';

describe('PostLike', () => {
  let service: PostLike;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostLike);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
