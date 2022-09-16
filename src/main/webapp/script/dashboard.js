var DASHBOARD = {
    loadDashboard: function () {

        let request = {

            url: "getDashboard",

            data: {},

            callback: DASHBOARD.loadDashboardData,

        };

        API.ajaxpost(request);

    },

    loadDashboardData: function (callbackContext) {

        let data = callbackContext.result.result

        $("#body").addClass("container row mx-auto p-3 px-5 d-flex justify-content-around")

        $("#body").html("")

        if (data.cpu !== undefined) {

            $("#body").append(COMPONENTS.card(

                "TOP N CPU usage",

                data.cpu,

                "3"

            ))
        }

        if (data.rtt !== undefined) {

            $("#body").append(COMPONENTS.card(

                "TOP N RTT",

                data.rtt,

                "3"

            ))

        }

        if (data.disk !== undefined) {

            $("#body").append(COMPONENTS.card(

                "TOP N DISK USAGE",

                data.disk,

                "3"
            ))
        }

        if (data.devices !== undefined) {

            $("#body").append(COMPONENTS.card(

                "Current Status of devices",

                '<table id="statusTable" class="table table-hover table-bordered display"></table>',

                "6"

            ))

            $('#statusTable').DataTable({

                data: data.devices,

                columns: [

                    {title: 'IP'},

                    {title: 'STATUS'},

                ],

            });

        }

    }
    
}