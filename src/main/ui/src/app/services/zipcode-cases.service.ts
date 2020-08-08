import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {Day} from '../day';
import { map } from 'rxjs/operators';
@Injectable({
  providedIn: 'root',
})
export class ZipcodeCasesService {
  private baseUrl = '';
  constructor(private http: HttpClient) { }
  getZipcodeCases(): Observable<Map<string,number >> {
    return this.http.get< Map<string,number>>(`${this.baseUrl}/zipcodes/cases`);
  }
  getZipcodeTimestamps(): Observable<Map<string,number >> {
    return this.http.get< Map<string,number>>(`${this.baseUrl}/zipcodes/timestamps`);
  }
}
