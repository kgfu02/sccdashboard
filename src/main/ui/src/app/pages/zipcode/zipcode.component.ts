import { Component, OnInit } from '@angular/core';
import {ZipcodeCasesService} from "../../services/zipcode-cases.service";
import 'rxjs/add/operator/map'
@Component({
  selector: 'ngx-zipcode',
  templateUrl: './zipcode.component.html',
  styleUrls: ['./zipcode.component.scss']
})
export class ZipcodeComponent implements OnInit {
  x: Map<string,number>
  constructor(private zipcodeCasesService:ZipcodeCasesService) {  }

  ngOnInit(): void {
    this.zipcodeCasesService.getZipcodeCases().subscribe( cases => {
      this.x = cases
      let a = '95129'
      console.log(this.x[a])
      this.x.clear()

    });
  }

}
