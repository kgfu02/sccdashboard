import {Timestamp} from 'rxjs';

export class Day {
  constructor(public id: number,
  public timestamp: Timestamp<any>,
  public count: number,
  public city: String) {}
}
