import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import { NbThemeService } from '@nebular/theme';
import {CasesService} from '../../../cases.service';
import {Observable} from 'rxjs';
import {Day} from '../../../day';
import { NbComponentShape, NbComponentSize, NbComponentStatus } from '@nebular/theme';

@Component({
  selector: 'ngx-echarts-line',
  template: `
    <div echarts (chartInit)="onChartInit($event)" [options]="options" class="echart"></div>
  `,
})
export class EchartsLineComponent implements AfterViewInit, OnDestroy {
  options: any = {};
  themeSubscription: any;
  days: Day[];
  chartInstance: any;
  sizing: NbComponentSize = 'medium';
  names: String[] = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
    'Milpitas', 'Morgan Hill', 'Mountain View',
    'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];

  constructor(private theme: NbThemeService, private casesService: CasesService) {
  }

  onChartInit(e: any) {
    this.chartInstance = e;
    e.resize();
    console.log('on chart init:', e);
  }
  ngAfterViewInit() {
    /*this.casesService.getDays().subscribe(
      response => {this.days = response; });*/
    this.casesService.invokeEvent.subscribe(val => {
        if(val==1) {this.selectAll();}

    });
    this.casesService.invokeEvent.subscribe(val => {
        if(val==0) {this.unselectAll()}
    });
    this.themeSubscription = this.theme.getJsTheme().subscribe(config => {

      const colors: any = config.variables;
      const echarts: any = config.variables.echarts;

      this.casesService.getTimestamps().subscribe(response => {
        this.casesService.getCityCases().subscribe(cases => {
          console.log(cases[0]);
        this.options = {
          backgroundColor: echarts.bg,
          color: ['#2993DA', '#8dced4', '#5d2e2c', '#9a0e35',
            '#857cd9', '#fae9d9', '#b8826d', '#5ee7ca',
            '#f605c1', '#85fd2b', '#f89c58', '#e1f0f5',
            '#fa2f83'],
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c}',
          },
          legend: {
            left: 'left',
            data: ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
              'Milpitas', 'Morgan Hill', 'Mountain View',
              'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'],
            textStyle: {
              color: echarts.textColor,
            },
          },
          dataZoom: {
            type: 'slider',
            textStyle: {
              color: '#FFFFFF',
            },
          },
          xAxis: [
            {
              // name: 'hello',
              // nameLocation: 'middle',
              type: 'category',
              data: response,
              axisTick: {
                alignWithLabel: true,
              },
              axisLine: {
                lineStyle: {
                  color: echarts.axisLineColor,
                },
              },
              axisLabel: {
                textStyle: {
                  color: echarts.textColor,
                },
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
              axisLine: {
                lineStyle: {
                  color: echarts.axisLineColor,
                },
              },
              splitLine: {
                lineStyle: {
                  color: echarts.splitLineColor,
                },
              },
              axisLabel: {
                textStyle: {
                  color: echarts.textColor,
                },
              },
            },
          ],
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true,
          },
          series: [
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Campbell',
              type: 'line',
              data: cases[0],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Cupertino',
              type: 'line',
              data: cases[1],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Gilroy',
              type: 'line',
              data: cases[2],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Los Altos',
              type: 'line',
              data: cases[3],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Los Gatos',
              type: 'line',
              data: cases[4],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Milpitas',
              type: 'line',
              data: cases[5],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Morgan Hill',
              type: 'line',
              data: cases[6],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Mountain View',
              type: 'line',
              data: cases[7],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Palo Alto',
              type: 'line',
              data: cases[8],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'San Jose',
              type: 'line',
              data: cases[9],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Santa Clara',
              type: 'line',
              data: cases[10],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Saratoga',
              type: 'line',
              data: cases[11],
            },
            {
              showAllSymbol: true,
              connectNulls: true,
              name: 'Sunnyvale',
              type: 'line',
              data: cases[12],
            },
          ],
        }; }); });

    });
  }

  selectAll() {
    console.log('hey');
    let arr = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
      'Milpitas', 'Morgan Hill', 'Mountain View',
      'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
    for( let i = 0; i < arr.length; i++) {
      this.chartInstance.dispatchAction({
        type: 'legendSelect',
        name: arr[i]
      });
    }
  }

  unselectAll() {
    console.log('hey');
    let arr = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
      'Milpitas', 'Morgan Hill', 'Mountain View',
      'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
    for( let i = 0; i < arr.length; i++) {
      this.chartInstance.dispatchAction({
        type: 'legendUnSelect',
        name: arr[i]
      });
    }
  }

  ngOnDestroy(): void {
    this.themeSubscription.unsubscribe();
  }
}
