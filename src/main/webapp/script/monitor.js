var MONITOR = {
    loadMonitor: function () {
        $("#body").removeClass()

        $("#body").html(`<div class="container"><div class="row"><div class="col-9 mx-auto bg-light shadow mt-5 p-3 rounded"><table id="monitorTable" class="table table-hover table-bordered display"></table></div></div></div>`)

        MONITOR.loadMonitorTable()

        $("#monitorTable").on("click", ".deleteMonitorButton", MONITOR.deleteMonitor);

        $("#monitorTable").on("click", ".editMonitorButton", function () {

            // DISCOVERY.getDeviceData($(this).data("id"))
            console.log($(this).data("id"))

        });

        $("#monitorTable").on("click", ".viewMonitorButton", function () {

            // MONITOR.deleteMonitor($(this).data("id"))
            console.log($(this).data("id"))

        });
    },

    loadMonitorTable: function (isUpdate = false) {

        $.get(
            "getMonitorDevices",

            function (data) {

                var dataSet = data.result.list

                if (isUpdate) $('#monitorTable').DataTable().destroy();

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

            }
        )
    },

    deleteMonitor: function () {

        $.post(

            "deleteMonitorDevice",

            {id: $(this).data("id")},

            function () {

                COMPONENTS.alert("Device Delete", "Device has been deleted", "danger")

                MONITOR.loadMonitorTable(true)

            }

        )

    }

}