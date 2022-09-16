var charts = {

    canvas: function (id) {

        return `<canvas id="${id}" style="width:100%"></canvas>`

    },

    chart: function (id, chart, labels, data, colors, text, limits = null) {

        if (colors == null) {

            colors = []

            for (let i = 0; i < data.length; i++) {

                let r = Math.floor(Math.random() * 255)

                let g = Math.floor(Math.random() * 255)

                let b = Math.floor(Math.random() * 255)

                colors.push(`rgba(${r},${g},${b})`)

            }

        }

        let scales = {}

        if (limits !== null) {

            scales = {

                y: {

                    beginAtZero: true,

                    min: limits[0],

                    max: limits[1],

                    ticks: {

                        precision: 0

                    }

                }

            }
        }

        var ctx = document.getElementById(id);

        new Chart(ctx, {

            type: chart,

            data: {

                labels: labels,

                datasets: [{

                    label: text,

                    data: data,

                    fill: false,

                    borderColor: 'rgb(75, 192, 192)',

                    tension: 0.1,

                    backgroundColor: colors,

                    hoverOffset: 4

                }]

            },

            options: {

                scales: scales

            }

        });

    }

}