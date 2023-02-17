import Chart from 'chart.js/auto'

/*
{
    "light": {
        "status": [
            {
                "time": "13:05:43",
                "status": true
            },
            {
                "time": "16:32:04",
                "status": false
            }
        ]
    },
    "rollerblind": {
        "status": [
            {
                "time": "13:05:12",
                "percentage": 90
            },
            {
                "time": "19:00:45",
                "percentage": 0
            }
        ]
    }
}
*/

let lightData = []
const chart = new Chart(document.getElementById('history'), {
  type: 'line',
  data: {
      labels: [],
      datasets: [{
          label: 'Roller blind',
          data: [],
          fill: false,
          borderColor: 'rgb(53, 215, 233)',
          tension: 0.1
      }, {
          data: [],
          pointRadius: 0,
          segment: {
              borderColor: ctx => lightData[ctx.p0DataIndex] ? 'rgb(53, 233, 82)' : 'rgb(235, 62, 62)',
          },
          fill: false,
          tension: 0
      }]
  },
  options: {
      plugins: {
          legend: {
              labels: {
                  font: {
                      size: 18
                  },
                  generateLabels: c => [
                      {
                          text: 'Roller blind',
                          fillStyle: 'rgb(53, 215, 233)',
                      }, {
                          text: 'Light ON',
                          fillStyle: 'rgb(53, 233, 82)'
                      }, {
                          text: 'Light OFF',
                          fillStyle: 'rgb(235, 62, 62)'
                      }
                  ]
              }
          },
      },
      responsive: true,
      maintainAspectRatio: false,
      scales: {
          y: {
              suggestedMin: -10,
              suggestedMax: 100
          },
          x: {
              type: 'time',
              time: {
                  unit: 'second'
              }
          }
      }
  }
});

function updateChart(data) {
  chart.data.labels = Object.keys(data).map(d => new Date(d));
  chart.data.datasets[0].data = Object.values(data).map(o => o.rollerBlind);
  lightData = Object.values(data).map(o => o.light);
  chart.data.datasets[1].data = new Array(lightData.length).fill(-10);
  chart.update();
}