import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {Day} from './day';
@Injectable({
  providedIn: 'root',
})
export class CasesService {
  private baseUrl = '';
  constructor(private http: HttpClient) { }
  invokeEvent: Subject<any> = new Subject();
  getDays(): Observable< Day[] > {
    return this.http.get< Day[]>(`${this.baseUrl}/days`);
  }

  getTimestamps(): Observable< String[] > {
    return this.http.get< String[]>(`${this.baseUrl}/days/timestamps`);
  }

  getCityCases(): Observable< String[][] > {
    return this.http.get< String[][]>(`${this.baseUrl}/days/cities`);
  }

  selAll(): void {
    console.log('hello world');
    this.invokeEvent.next(1);
  }
  unselAll(): void {
    console.log('qweryt');
    this.invokeEvent.next(0);
  }
}
