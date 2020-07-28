import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {KeyMetric} from "../KeyMetric";

@Injectable({
  providedIn: 'root'
})
export class KeyMetricService {
  private baseUrl = '';
  constructor(private http: HttpClient) { }
  invokeEvent: Subject<any> = new Subject();
  getKeyMetric(type:string): Observable< KeyMetric[] > {
    return this.http.get< KeyMetric[]>(`${this.baseUrl}/keyMetric/${type}`);
  }

  getKeyMetricDate(type:string): Observable< String > {
    return this.http.get(`${this.baseUrl}/keyMetric/${type}/date`, {responseType:'text'} );
  }
}
