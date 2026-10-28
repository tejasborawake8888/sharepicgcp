
import { isDevMode } from '@angular/core';

const local = isDevMode();

export const API_CONFIG = {
  user: local ? 'http://localhost:8081/api/v1' : '/api/v1',
  post: local ? 'http://localhost:8080/api/v1' : '/api/v1',
  feed: local ? 'http://localhost:8082/api/v1' : '/api/v1'
};
