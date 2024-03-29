import {AfterViewInit, Component, OnInit} from '@angular/core';
import { CasesService} from "../../../services/cases.service";
import { NbComponentShape, NbComponentSize, NbComponentStatus } from '@nebular/theme';
import { HostListener } from "@angular/core";
import {Subject} from "rxjs";
import {KeyMetricService} from "../../../services/key-metric.service";
import {DatePipe} from "@angular/common";

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
  populations: number[] = [42470, 60614, 55525, 30588, 30922,
    77457, 43876, 80993, 67019, 1026658, 126209, 30886, 152323];
  screenHeight: number;
  screenWidth: number;
  chartHeight: string;
  mode: string = 'total';
  totalCases = new Array(3);
  totalDeaths = new Array(3);
  newCases: any
  currentDay: string
  latestMetricDay: string
  latestCityDay: string
  buttonText: string = "per 100,000"; //changed to 100k if mobile

  constructor(private casesService: CasesService, private keyMetricService: KeyMetricService, private datePipe: DatePipe,private datePipe2: DatePipe) {
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
    if (this.screenWidth < 1000)
      this.buttonText = "per 100k"
  }

  ngAfterViewInit() {
    console.log("hello");
    this.casesService.getTimestamps().subscribe(data => {this.latestCityDay = ""+data[data.length-1];console.log(this.currentDay,this.latestCityDay)});
    this.keyMetricService.getKeyMetricDate("totalCases").subscribe(data => {this.latestMetricDay = ""+data });
    this.currentDay = this.datePipe.transform(new Date().toLocaleString("en-US", {timeZone: "America/Los_Angeles"}), "yyyy/MM/dd");



    this.casesService.getCityCases().subscribe(data => {
      for (let i = 0; i < 13; i++) {
        this.todaycounts.push(data[i][data[i].length-1]);
        // find most recent non-null entry
        let offset = 2.0
        while(data[i][data[i].length-offset]==null)
          offset++
        if(data[i][data[i].length-offset] != null) {
          let avgChange: number
          avgChange = (Number(data[i][data[i].length-1])-Number(data[i][data[i].length-offset]))/(offset-1)
          this.todaychange.push(Math.round(avgChange/this.populations[i]* 100000 * 100) / 100);
        }
        if (i==1) {
          console.log("change array")
          console.log(Number(data[i][data[i].length-1]))

          console.log(Number(data[i][data[i].length-8]))
        }
      }
      console.log("data is");
      console.log(data);
      console.log("change is");
      console.log(this.todaychange);
    });

    this.keyMetricService.getKeyMetric("totalCases").subscribe(cases => {
      console.log(cases);
      this.totalCases[0] = cases[0];
      this.totalCases[1] = cases[1];
      if (cases[0].metric>cases[1].metric)
        this.totalCases[2] = "+";
      else
        this.totalCases[2] = "";
    })
    this.keyMetricService.getKeyMetric("newCases").subscribe(newCases => {
      this.newCases = newCases[0].metric;
    })
    this.keyMetricService.getKeyMetric("totalDeaths").subscribe(deaths => {
      this.totalDeaths[0] = deaths[0];
      this.totalDeaths[1] = deaths[1];
      if (deaths[0].metric>deaths[1].metric)
        this.totalDeaths[2] = "+";
      else
        this.totalDeaths[2] = "";
    })
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
    if(this.mode=='per') {
      this.mode='total';
      this.parentSubject.next('total');
      return;
    }
    this.mode = 'per';
    this.parentSubject.next('per');
  }
  ToggleNewOn() {

    this.mode = 'new';
    this.parentSubject.next('new');
  }
  ToggleNewPerOn() {
    if(this.mode=='newPer') {
      this.mode='new';
      this.parentSubject.next('new');
      return;
    }
    this.mode = 'newPer';
    this.parentSubject.next('newPer');
  }
}
