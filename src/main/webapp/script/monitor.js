var MONITOR = {
    loadMonitor: function () {
        $("#body").removeClass()

        $("#body").html(`<div class="container"><div id="visual"><div class="row pt-4 d-flex justify-content-between align-items-center"><div class="col-auto"><button class="btn btn-outline-danger" onclick="MONITOR.showMonitorTable()">BACK</button></div><div class="col-auto" id="deviceInfo">10.20.40.224</div><hr><div class="row d-flex justify-content-end" id="actions"></div><div class="row d-flex justify-content-evenly" id="graph"></div></div></div><div id="table"><div class="row"><div class="col-9 mx-auto bg-light shadow mt-5 p-3 rounded"><table id="monitorTable" class="table table-hover table-bordered display"></table></div></div></div></div>`)

        $("#body").on("click", ".pingNow", MONITOR.pingDevice);

        $("#monitorTable").on("click", ".deleteMonitorButton", MONITOR.deleteMonitorModal);

        $("#monitorTable").on("click", ".viewMonitorButton", MONITOR.viewMonitor);

        let request = {
            url: "getMonitorDevices",
            data: {},
            callback: MONITOR.loadMonitorTable,
        };

        API.ajaxpost(request);
    },

    loadMonitorTable: function (data, isUpdate = false) {

        if (isUpdate) {
            let request = {
                url: "getMonitorDevices",
                data: {},
                callback: MONITOR.loadMonitorTable,
            };

            $('#monitorTable').DataTable().destroy()
            API.ajaxpost(request)
            return
        }

        var dataSet = data.result.list

        $('#monitorTable').html(`<table id="monitorTable" class="table table-hover table-bordered display"></table>`)

        $('#monitorTable').DataTable({

            data: dataSet,

            columns: [

                {title: 'Device Name'},

                {title: 'IP'},

                {title: 'TYPE'},

                {title: 'ACTIONS'}

            ],

        });
    },

    showMonitorTable: function () {

        $("#table").css("display", "block")

        $("#visual").css("display", "none")

    },

    viewMonitor: function () {
        let request = {
            url: "getMetrics",
            data: {ip: $(this).data("ip"), type: $(this).data("type")},
            callback: MONITOR.loadMetricData,
        };

        API.ajaxpost(request);
    },

    loadMetricData: function (data) {
        if (data.result.code === 0) {
            COMPONENTS.alert("Polling Status", data.result.status, "danger")

            return
        }

        $("#table").css("display", "none")

        $("#visual").css("display", "block")

        $("#deviceInfo").html(data.result.ip)

        $("#actions").html(data.result.actions)

        $("#lastPollTime").html("Last poll time: "+data.result.timestamp)

        $("#graph").html(COMPONENTS.card("Availability", CHARTS.canvas("availability"), "3"))

        $("#graph").append("<div class='col-3' id='memrtt'></div>")

        if (data.type === "ssh") {

            $("#graph").append(COMPONENTS.card("Memory", CHARTS.canvas("memory"), "3"))

            $("#memrtt").append(COMPONENTS.card("Storage", `<h3>${data.result.disk === "" ? "<span class='text-danger'>N/A</span>" : `<span class="">${data.result.disk} used</span>`}</h3>`, "12", "null"))

            CHARTS.chart("memory", "doughnut", ["Used Memory [MB]", "Free Memory [MB]"], [data.result.mem, data.result.total_mem - data.result.mem], ["pink", "cyan"])

        }

        $("#memrtt").append(COMPONENTS.card("RTT", `<h3>${data.result.rtt === undefined ? "<span class='text-danger'>N/A</span>" : `<span class="text-success">${data.result.rtt} ms</span>`}</h3>`, "12", "null"))

        $("#graph").append(COMPONENTS.card("Ping Chart", CHARTS.canvas("pingchart"), "5"))

        CHARTS.chart("availability", "doughnut", ["UP %", "DOWN %"], [data.result.availability, 100 - data.result.availability], ["blue", "red"])

        CHARTS.chart("pingchart", "bar", data.result.time.map(ele => ele.split(" ")[1]), data.result.packets, Array(data.result.packets.length).fill("#80c6ff"), "Received packets out of 4", [0, 4])

        if (data.type === "ssh") {

            $("#graph").append(COMPONENTS.card("CPU Usage", CHARTS.canvas("cpu"), "6"))

            CHARTS.chart("cpu", "line", data.result.time.map(ele => ele.split(" ")[1]), data.result.cpu, Array(data.result.cpu.length).fill("#80c6ff"), "CPU Utilization in %", [0, 100])
        }
    },

    deleteMonitorModal: function (){

        COMPONENTS.modal("Delete device", "Delete", "MONITOR.deleteMonitor",$(this).data("id"))

        $('#modalBody').html("Do you want to delete this monitor ?");

        $('#modal').modal('toggle');

    },

    deleteMonitor: function (id) {

        $('#modal').modal('toggle');

        let request = {
            url: "deleteMonitorDevice",
            data: {id},
            callback: MONITOR.sendNotification,
            async: false
        };

        API.ajaxget(request,false);

        MONITOR.loadMonitorTable(null,true)
    },

    pingDevice: function () {

        let request = {
            url: "deviceStatus",
            data: {ip: $(this).data("ip"),socketId: localStorage.getItem("socketId")},
            callback: MONITOR.sendNotification,
        };

        API.ajaxget(request);
    },

    sendNotification: function (data) {
        COMPONENTS.alert("Monitor", data.result.status, data.result.code === 1 ? "success" : "danger");
    }

}