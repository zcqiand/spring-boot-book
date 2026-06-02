// ECharts 封装组件
Vue.component('ChartPanel', {
  props: {
    title: String,
    chartType: {
      type: String,
      default: 'line'  // line, bar, pie, gauge
    },
    data: {
      type: Array,
      default: () => []
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  template: `
    <div class="chart-panel">
      <div class="chart-title">{{ title }}</div>
      <div ref="chartDom" :style="{ height: height }"></div>
    </div>
  `,
  mounted() {
    this.initChart();
  },
  watch: {
    data: {
      handler() {
        this.updateChart();
      },
      deep: true
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chartDom);
      this.updateChart();
    },
    updateChart() {
      if (!this.chart) return;

      const option = this.getChartOption();
      this.chart.setOption(option);
    },
    getChartOption() {
      switch (this.chartType) {
        case 'line':
          return this.getLineOption();
        case 'bar':
          return this.getBarOption();
        case 'pie':
          return this.getPieOption();
        case 'gauge':
          return this.getGaugeOption();
        default:
          return {};
      }
    },
    getLineOption() {
      const xData = this.data.map(d => d.x);
      const yData = this.data.map(d => d.y);

      return {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'line',
          data: yData,
          smooth: true,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 115, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 115, 255, 0.05)' }
            ])
          },
          lineStyle: {
            color: '#407FFF',
            width: 2
          },
          itemStyle: {
            color: '#407FFF'
          }
        }]
      };
    },
    getBarOption() {
      const xData = this.data.map(d => d.x);
      const yData = this.data.map(d => d.y);

      return {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: xData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          type: 'bar',
          data: yData,
          barWidth: '60%',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#407FFF' },
              { offset: 1, color: '#6B8FFF' }
            ])
          }
        }]
      };
    },
    getPieOption() {
      return {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['35%', '50%'],
          data: this.data,
          label: {
            show: true,
            formatter: '{b}: {c}'
          }
        }]
      };
    },
    getGaugeOption() {
      const value = this.data[0]?.value || 0;
      return {
        series: [{
          type: 'gauge',
          startAngle: 180,
          endAngle: 0,
          center: ['50%', '70%'],
          radius: '90%',
          min: 0,
          max: 100,
          splitNumber: 10,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#FF6B6B' },
              { offset: 0.5, color: '#FFD93D' },
              { offset: 1, color: '#6BCB77' }
            ])
          },
          progress: {
            show: true,
            width: 18
          },
          pointer: {
            show: false
          },
          axisLine: {
            lineStyle: {
              width: 18
            }
          },
          axisTick: {
            show: false
          },
          splitLine: {
            show: false
          },
          axisLabel: {
            show: false
          },
          title: {
            show: true,
            offsetCenter: [0, '-10%'],
            fontSize: 12
          },
          detail: {
            valueAnimation: true,
            fontSize: 24,
            offsetCenter: [0, '10%'],
            formatter: '{value}%'
          },
          data: [{
            value: value,
            name: this.data[0]?.name || ''
          }]
        }]
      };
    }
  }
});