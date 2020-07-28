import {Timestamp} from 'rxjs';

export class KeyMetric {
  constructor(public id: number,
              public timestamp: Timestamp<any>,
              public metric: number,
              public type: String) {}
}
