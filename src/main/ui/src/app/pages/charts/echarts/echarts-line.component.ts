import {AfterViewInit, Component, HostListener, Input, OnDestroy} from '@angular/core';
import {NbComponentSize, NbThemeService} from '@nebular/theme';
import {CasesService} from '../../../services/cases.service';
import {Subject} from 'rxjs';
import {Day} from '../../../day';

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
  timestamps: String[];
  chartInstance: any;
  sizing: NbComponentSize = 'medium';
  names: String[] = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
    'Milpitas', 'Morgan Hill', 'Mountain View',
    'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
  screenHeight: number;
  screenWidth: number;
  gridTop: string = '17%'; // distance from top of grid to top border
  @Input()
  parentSubject: Subject<any>;

  constructor(private theme: NbThemeService, private casesService: CasesService) {
    this.getScreenSize();
  }

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
    console.log(this.screenHeight, this.screenWidth);
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
      if (val === 1) {
        this.selectAll();
      }

    });
    this.casesService.invokeEvent.subscribe(val => {
      if (val === 0) {
        this.unselectAll();
      }
    });
    this.themeSubscription = this.theme.getJsTheme().subscribe(config => {

      const colors: any = config.variables;
      const echarts: any = config.variables.echarts;

      this.casesService.getTimestamps().subscribe(response => {
        this.casesService.getCityCases().subscribe(cases => {
          //console.log(cases[0]);
          this.timestamps = response;
          if (this.screenWidth < 1000) {
            this.gridTop = '28%';
          }
          this.options = {
            backgroundColor: echarts.bg,
            color: ['#259eee', '#8dced4', '#5d2e2c', '#9a0e35',
              '#726aa3', '#0028ff', '#ffc1a5', '#045704',
              '#ffff54', '#85fd2b', '#aa560e', '#808000',
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
                fontSize: 12.5,
              },
              padding: 0,
              itemGap: 13,
              itemWidth: 28,
              itemHeight: 14,
              icon: 'circle',
            },
            dataZoom: {
              type: 'slider',
              textStyle: {
                color: '#FFFFFF',
              },
              handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
              handleSize: '100%',
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
              top: this.gridTop,
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
                symbolSize: 6,
                data: cases[0],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Cupertino',
                type: 'line',
                symbolSize: 6,
                data: cases[1],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Gilroy',
                type: 'line',
                symbolSize: 6,
                data: cases[2],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Los Altos',
                type: 'line',
                symbolSize: 6,
                data: cases[3],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Los Gatos',
                type: 'line',
                symbolSize: 6,
                data: cases[4],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Milpitas',
                type: 'line',
                symbolSize: 6,
                data: cases[5],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Morgan Hill',
                type: 'line',
                symbolSize: 6,
                data: cases[6],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Mountain View',
                type: 'line',
                symbolSize: 6,
                data: cases[7],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Palo Alto',
                type: 'line',
                symbolSize: 6,
                data: cases[8],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'San Jose',
                type: 'line',
                symbolSize: 6,
                data: cases[9],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Santa Clara',
                type: 'line',
                symbolSize: 6,
                data: cases[10],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Saratoga',
                type: 'line',
                symbolSize: 6,
                data: cases[11],
              },
              {
                showAllSymbol: true,
                connectNulls: true,
                name: 'Sunnyvale',
                type: 'line',
                symbolSize: 6,
                data: cases[12],
              },
            ],
          };
        });
      });

    });
    this.parentSubject.subscribe(event => {
      console.log(event)
      if (event == 'total')
        this.toggleRawOn();
      else if (event == 'per') // per 100000
        this.togglePerOn();
      else
        this.toggleNewOn();
    });
  }

  populations: number[] = [42470, 60614, 55525, 30588, 30922,
    77457, 43876, 80993, 67019, 1026658, 126209, 30886, 152323];

  toggleRawOn() {
    this.casesService.getCityCases().subscribe(cases => {
      this.setData(cases,"raw");
    });
  }

  togglePerOn() {
    this.casesService.getCityCases().subscribe(cases => {
        for (let i = 0; i<cases.length; i++) {
          for (let j = 0; j<cases[i].length; j++) {
            if(cases[i][j]!=null)
              cases[i][j] = String(Math.round(+cases[i][j] / this.populations[i] * 100000*100)/100);
            else
              cases[i][j] = null;
          }
        }
        this.setData(cases,"per");
      }
    );
  }

  /*toggleNewOn() {
    this.casesService.getCityCases().subscribe(cases => {
        var newCases: String[][] = []
        for (let i = 0; i<cases.length; i++) {
          newCases.push([])
          for (let j = 7; j<cases[i].length; j++) { // 7 to offset initial null values
            if(cases[i][j]!=null && cases[i][j-1]!=null)
              newCases[i].push(String(+cases[i][j]-(+cases[i][j-1]))) // + converts string to num?
            else
              newCases[i].push(null)
          }
          console.log(newCases)
        }
        this.setData(newCases,"new")
      }
    );
    // set categories
  }*/

  toggleNewOn() { // 7 day avg
    this.casesService.getCityCases().subscribe(cases => {
        var newCases: String[][] = []
        for (let i = 0; i<cases.length; i++) {
          newCases.push([])
          for (let j = 7; j<cases[i].length; j++) { // 7 to offset initial null values
            if(cases[i][j]!=null && cases[i][j-7]!=null)
              newCases[i].push(String(Math.round((+cases[i][j]-(+cases[i][j-7]))/7.0*10)/10 ))  // + converts string to num?
            else
              newCases[i].push(null)
          }
          console.log(newCases)
        }
        this.setData(newCases,"new")
      }
    );
    // set categories
  }

  setData(cases: String[][], type: string) {
    for (let i = 0; i<cases.length; i++) {
      this.chartInstance.setOption({
        series: [
          {
            name: this.names[i],
            data: cases[i]
          }
        ]
      })
    }
    if (type == "new") {
      this.chartInstance.setOption({
        xAxis: [
          {
            type: 'category',
            data: this.timestamps.slice(7), //slice to offset null timestampos at beginning
          }
        ]
      });
    }
    else {
      this.chartInstance.setOption({
        xAxis: [
          {
            type: 'category',
            data: this.timestamps,
          }
        ]
      });
    }
  }

  selectAll() {
    console.log('hey');
    let arr = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
      'Milpitas', 'Morgan Hill', 'Mountain View',
      'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
    for (let i = 0; i < arr.length; i++) {
      this.chartInstance.dispatchAction({
        type: 'legendSelect',
        name: arr[i],
      });
    }
  }

  unselectAll() {
    console.log('hey');
    let arr = ['Campbell', 'Cupertino', 'Gilroy', 'Los Altos', 'Los Gatos',
      'Milpitas', 'Morgan Hill', 'Mountain View',
      'Palo Alto', 'San Jose', 'Santa Clara', 'Saratoga', 'Sunnyvale'];
    for (let i = 0; i < arr.length; i++) {
      this.chartInstance.dispatchAction({
        type: 'legendUnSelect',
        name: arr[i],
      });
    }
  }

  ngOnDestroy(): void {
    this.themeSubscription.unsubscribe();
  }
}
