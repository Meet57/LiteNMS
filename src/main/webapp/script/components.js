var COMPONENTS = {
    card: function (title, body, col = "auto") {
        return `
            <div class="col-${col} card my-2 shadow p-0 h-100">
                <div class="card-header h3">
                        ${title}
                </div>
                <div class="card-body p-3">
                    <div class="card-text">
                        ${body}
                    </div>
                </div>
            </div>
        `
    },
    table: function (title, content) {

        var table = `
        <table class="w-100 table table-striped table-hover mt-3"><thead class="table-dark"><tr>
        `
        title.forEach(title => {
            table += `
                <td>${title}</td>
            `
        });

        table += `</tr></thead><tbody>`

        content.forEach(row => {
            table += '<tr>'
            row.forEach(data => {
                table += `<td>${data}</td>`
            })
            table += '</tr>'
        })

        table += '</tbody></table>'

        return table
    },
    modal: function (header, buttonName, buttonFunction) {
        $("#modalHeading").html(header)
        $("#modalSubmitButton").html(buttonName)
        $("#modalSubmitButton").attr("onclick", buttonFunction + "()");
    },
    alert: function (title, body, type = "primary") {
        var html = `
            <div class="position-fixed top-0 end-0 p-3" style="z-index: 99999">
              <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="toast-header bg-${type}">
                  <strong class="me-auto text-white">${title}</strong>
                  <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
                <div class="toast-body">
                  ${body}
                </div>
              </div>
            </div>
        `
        $("#alert").html(html)
        $(document).ready(function () {
            $("#liveToast").toast("show");
        });
    }
}

var CHARTS = {
    canvas: function (id) {
        return `<canvas id="${id}" style="width:100%"></canvas>`
    }
    ,
    chart: function (id, chart, labels, data, colors, text) {
        if (colors == null) {
            colors = []
            for (let i = 0; i < data.length; i++) {
                let r = Math.floor(Math.random() * 255)
                let g = Math.floor(Math.random() * 255)
                let b = Math.floor(Math.random() * 255)
                colors.push(`rgba(${r},${g},${b})`)
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
        });
    }
}