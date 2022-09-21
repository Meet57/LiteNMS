var monitor = {
    loadMonitor: function () {
        $("#body").removeClass()

        $("#body").html(`<div class="container"><div id="visual"><div class="row pt-4 d-flex justify-content-between align-items-center"><div class="col-auto"><button class="btn btn-outline-danger" onclick="monitor.showMonitorTable()">BACK</button></div><div class="col-auto" id="deviceInfo">10.20.40.224</div><hr><div class="row d-flex justify-content-end" id="actions"></div><div class="row d-flex justify-content-evenly" id="graph"></div></div></div><div id="table"><div class="row"><div class="col-9 mx-auto bg-light shadow mt-5 p-3 rounded"><table id="monitorTable" class="table table-hover table-bordered display"></table></div></div></div></div>`)

        $("#body").on("click", ".pingNow", monitor.pingDevice);

        $("#body").on("click", ".pollNow", monitor.pollDevice);

        $("#monitorTable").on("click", ".deleteMonitorButton", monitor.deleteMonitorModal);

        $("#monitorTable").on("click", ".viewMonitorButton", monitor.viewMonitor);

        let request = {
            url: "getMonitorDevices",
            data: {},
            callback: monitor.loadMonitorTable,
        };

        api.ajaxpost(request);
    },

    loadMonitorTable: function (data, isUpdate = false) {

        if (isUpdate) {

            let request = {

                url: "getMonitorDevices",

                data: {},

                callback: monitor.loadMonitorTable,

            };

            $('#monitorTable').DataTable().destroy()

            api.ajaxpost(request)

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

            data: {device_id: $(this).data("id"), type: $(this).data("type")},

            callback: monitor.loadMetricData,
        };

        api.ajaxpost(request);

    },

    loadMetricData: function (data,socketUpdate = false) {

        if (data.result.code === 0) {

            components.alert("Polling Status", data.result.status, "danger")

            return

        }

        if(!socketUpdate){

            $("#table").css("display", "none")

            $("#visual").css("display", "block")

        }

        $("#deviceInfo").html(data.result.ip)

        $("#actions").html(data.result.actions)

        $("#lastPollTime").html("Last poll time: " + data.result.timestamp)

        $("#graph").html(components.card("Availability", charts.canvas("availability"), "3"))

        $("#graph").append("<div class='col-3' id='memoryRTT'></div>")

        if (data.type === "ssh") {

            $("#graph").append(components.card("Memory", charts.canvas("memory"), "3"))

            $("#memoryRTT").append(components.card("Storage", `<h3>${data.result.disk === "" ? "<span class='text-danger'>N/A</span>" : `<span class="">${data.result.disk} used</span>`}</h3>`, "12", "null"))

            charts.chart("memory", "doughnut", ["Used Memory [MB]", "Free Memory [MB]"], [data.result.mem, data.result.total_mem - data.result.mem], ["pink", "cyan"])

        }

        $("#memoryRTT").append(components.card("RTT", `<h3>${data.result.rtt === undefined ? "<span class='text-danger'>N/A</span>" : `<span class="text-success">${data.result.rtt} ms</span>`}</h3>`, "12", "null"))

        $("#graph").append(components.card("Ping Chart", charts.canvas("pingChart"), "5"))

        charts.chart("availability", "doughnut", ["UP %", "DOWN %"], [data.result.availability, 100 - data.result.availability], ["blue", "red"])

        charts.chart("pingChart", "bar", data.result.time.map(ele => ele.split(" ")[1]), data.result.packets, Array(data.result.packets.length).fill("#80c6ff"), "Received packets out of 4", [0, 4])

        if (data.type === "ssh") {

            $("#graph").append(components.card("CPU Usage", charts.canvas("cpu"), "6"))

            charts.chart("cpu", "line", data.result.time.map(ele => ele.split(" ")[1]), data.result.cpu, Array(data.result.cpu.length).fill("#80c6ff"), "CPU Utilization in %", [0, 100])

        }

    },

    deleteMonitorModal: function () {

        components.modal("Delete device", "Delete", "monitor.deleteMonitor", $(this).data("id"))

        $('#modalBody').html("Do you want to delete this monitor ?");

        $('#modal').modal('toggle');

    },

    deleteMonitor: function (id) {

        $('#modal').modal('toggle');

        let request = {

            url: "deleteMonitorDevice",

            data: {id},

            callback: monitor.sendNotification,

            async: false

        };

        api.ajaxget(request, false);

        monitor.loadMonitorTable(null, true)

    },

    pingDevice: function () {

        let request = {

            url: "deviceStatus",

            data: {

                ip: $(this).data("ip"),

                socketId: localStorage.getItem("socketId")

            },

        };

        api.ajaxget(request);

    },

    pollDevice: function (){

        let request = {

            url: "pollDevice",

            data: {

                device_id: $(this).data("id"),

                socketId: localStorage.getItem("socketId")

            },

        };

        api.ajaxget(request);

    },

    sendNotification: function (data) {

        components.alert("Monitor", data.result.status, data.result.code);

    }

}