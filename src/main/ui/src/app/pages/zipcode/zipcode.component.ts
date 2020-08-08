import { Component, OnInit } from '@angular/core';
import {ZipcodeCasesService} from "../../services/zipcode-cases.service";
import 'rxjs/add/operator/map'
import {NbThemeService} from "@nebular/theme";
import {Subject} from "rxjs";
@Component({
  selector: 'ngx-zipcode',
  templateUrl: './zipcode.component.html',
  styleUrls: ['./zipcode.component.scss']
})
export class ZipcodeComponent implements OnInit {
  options: any = {};
  map: Map<string,number>
  zipcode: String
  chartInstance: any;
  themeSubscription: any;
  echarts: any
  zipcodes: Array<string> = []
  mode: string = 'total'
  rowLen: number
  populations: any
  timestamps: any;

  constructor(private theme: NbThemeService, private zipcodeCasesService:ZipcodeCasesService) {  }

  ngOnInit(): void {
    this.zipcodeCasesService.getZipcodeCases().subscribe( cases => {
      this.map = cases
      let a = '95129'
      console.log(this.map[a])
    });
    this.themeSubscription = this.theme.getJsTheme().subscribe(config => {

      const colors: any = config.variables;
      const echarts: any = config.variables.echarts;
      this.zipcodeCasesService.getZipcodeTimestamps().subscribe(timestamps => {
        this.timestamps = timestamps
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
              data: timestamps,
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
        };
      })
    })
    this.populations = new Map([
      ["95138",20766 ],
      ["95120",37774 ],
      ["95136", 47599],
      ["95123",67186 ],
      ["94304",3902 ],
      ["94085", 23612],
      ["95148", 48854],
      ["95127", 65686],
      ["95119", 10472],
      ["95014", 62430],
      ["94043", 31488],
      ["95129", 39828],
      ["95128", 37665],
      ["95117", 29592],
      ["95037", 51652],
      ["95008", 46513],
      ["95134", 27224],
      ["95139", 7293],
      ["95110", 20203],
      ["95070", 31308],
      ["95131", 31170],
      ["95054", 24264],
      ["94089", 22313],
      ["95013", 77],
      ["95032", 26281	],
      ["95112", 61060],
      ["95035", 77562],
      ["95126", 35988],
      ["95113", 1939],
      ["94022", 19378],
      ["95140", 115],
      ["95135", 22358],
      ["95111", 62392],
      ["94086", 49630],
      ["95118", 32560],
      ["94024", 23961],
      ["95116", 56481],
      ["94305", 15730],
      ["95053", 3678],
      ["95020", 63852],
      ["95132", 41844],
      ["95051", 58123],
      ["95125", 53574],
      ["95050", 39452],
      ["94040", 35845],
      ["95002", 2146],
      ["95130", 14863],
      ["95122", 57780],
      ["95121", 38102],
      ["95030", 13288],
      ["95133", 28726],
      ["94087", 57219],
      ["94301", 17191],
      ["95124", 51170],
      ["94041", 14394],
      ["94306", 27549],
      ["95046", 6025],
    ]);
  }

  onChartInit(e: any) {
    this.chartInstance = e;
    e.resize();
    console.log('on chart init:', e);
  }

  formEnter(event: any): void {
    console.log(event.target.value)
    if (this.map[event.target.value]!=null) {
      console.log("valid key")
      this.setZipcode(event.target.value)
    }
    else {
      console.log("invalid key")
    }
  }

  setZipcode(zipcode:string) {
    console.log("switch")
    if(this.zipcodes.includes(zipcode)) {
      return
    }
    else {
      this.zipcodes.push(zipcode)
    }
    let input = Array()
    for (let i = 0; i<this.zipcodes.length; i++) {
      input.push({
        showAllSymbol: true,
        connectNulls: true,
        name: this.zipcodes[i],
        type: 'line',
        symbolSize: 6,
        data: this.map[this.zipcodes[i]]
      })
    }
    this.chartInstance.setOption({
      legend: {
        left: 'left',
        data: this.zipcodes,
        textStyle: {
          fontSize: 12.5,
        },
        padding: 0,
        itemGap: 13,
        itemWidth: 28,
        itemHeight: 14,
        icon: 'circle',
      },
      series: input
    })
    console.log(this.mode)
    if (this.mode=='per') {
      this.TogglePerOn()
    }
    if (this.mode=='new') {
      this.ToggleNewOn()
    }
  }
  // total = cumulative

  ToggleRawOn() {
    this.mode = 'total';
    var output: number[][] = []
    for (let i = 0; i<this.zipcodes.length; i++) {
      output.push([])
      for (let j = 0; j<this.map['95129'].length; j++) {
        output[i].push(this.map[this.zipcodes[i]][j])
      }
    }
    this.setData(output,'')
  }
  TogglePerOn() {
    console.log('per')
    this.mode = 'per';
    var output: number[][] = []
    for (let i = 0; i<this.zipcodes.length; i++) {
      output.push([])
      for (let j = 0; j<this.map['95129'].length; j++) {
        output[i].push(Math.round(this.map[this.zipcodes[i]][j]/this.populations.get(this.zipcodes[i])*100000*100)/100)
      }
    }
    console.log(output)
    this.setData(output,'')
  }
  ToggleNewOn() {
    this.mode = 'new';
    var output: number[][] = []
    for (let i = 0; i<this.zipcodes.length; i++) {
      output.push([])
      for (let j = 7; j<this.map['95129'].length; j++) {
        output[i].push(Math.round((this.map[this.zipcodes[i]][j]-this.map[this.zipcodes[i]][j-7])/7*100)/100)
      }
    }
    this.setData(output,'new')
  }
  setData(cases: number[][], type: string) {
    console.log(cases)
    for (let i = 0; i<this.zipcodes.length; i++) {
      this.chartInstance.setOption({
        series: [
          {
            name: this.zipcodes[i],
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
            data: this.timestamps.slice(7),
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
}

