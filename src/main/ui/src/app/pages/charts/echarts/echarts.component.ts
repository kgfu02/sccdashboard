import {AfterViewInit, Component, OnInit} from '@angular/core';
import { CasesService} from "../../../cases.service";
import { NbComponentShape, NbComponentSize, NbComponentStatus } from '@nebular/theme';
import { HostListener } from "@angular/core";
import {Subject} from "rxjs";

@Component({
  selector: 'ngx-echarts',
  styleUrls: ['./echarts.component.scss'],
  templateUrl: './echarts.component.html',
})
export class EchartsComponent implements AfterViewInit{

  sizing: NbComponentSize = 'tiny';
  hi = 'hi';
  names: String[] = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
    'Milpitas', 'Morgan Hill', 'Mountain View',
    'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
  todaycounts = new Array();
  todaychange = new Array();
  numbers: number[] = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
  screenHeight: number;
  screenWidth: number;
  chartHeight: string;
  mode: string = 'total';

  constructor(private casesService: CasesService) {
    this.getScreenSize();
  }
  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
    console.log(this.screenHeight, this.screenWidth);
    if (this.screenWidth < 600) {
      this.chartHeight = 'height: 500px';
    }
    else if (this.screenHeight<600) {
      this.chartHeight = 'height: 375px';
    }
  }

  ngAfterViewInit() {

    console.log("hello");
    this.casesService.getCityCases().subscribe(data => {
      for (let i = 0; i < 13; i++) {
        this.todaycounts.push(data[i][data[i].length-1]);
        if(data[i][data[i].length-2] != null) {
          this.todaychange.push( Number(data[i][data[i].length-1])-Number(data[i][data[i].length-2]) );
          console.log('yas');
        }
      }

      console.log(this.todaycounts);
    });
  }

  selectAll(): void {
    this.casesService.selAll();
  }
  unselectAll(): void {
    this.casesService.unselAll();
  }

  // total = cumulative
  parentSubject: Subject<any> = new Subject();
  ToggleRawOn() {
    this.mode = 'total';
    this.parentSubject.next('total');
  }
  TogglePerOn() {
    this.mode = 'per';
    this.parentSubject.next('per');
  }
  ToggleNewOn() {
    this.mode = 'new';
    this.parentSubject.next('new');
  }
}
