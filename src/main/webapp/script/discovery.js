var DISCOVERY = {

    loadDiscovery: function () {

        $("#body").removeClass()

        $("#body").html(`<div class="container"><div class="modal fade" id="modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"><div class="modal-dialog modal-dialog-scrollable"><div class="modal-content"><div class="modal-header"><h5 class="modal-title" id="modalHeading"></h5></div><div class="modal-body" id="modalBody">...</div><div class="modal-footer" id="modalFooter"><button type="submit" class="btn btn-secondary" data-bs-dismiss="modal">Close</button><button type="button" class="btn btn-primary" id="modalSubmitButton" onclick="DISCOVERY.addDeviceAction()"></button></div></div></div></div><div class="row"><div class="col-9 mx-auto mt-2 py-3"><button type="button" class="btn btn-primary" onclick="DISCOVERY.modelForm()">Add Device</button></div></div><div class="row"><div class="col-9 mx-auto bg-light shadow mt-2 p-3 rounded"><table id="discoveryTable" class="table table-hover table-bordered display"></table></div></div></div>`)

        DISCOVERY.loadDiscoveryTable()

        $("#discoveryTable").on("click", ".deleteButton", DISCOVERY.deleteDeviceAction);

        $("#discoveryTable").on("click", ".editButton", DISCOVERY.getDeviceData);

        $("#discoveryTable").on("click", ".runButton", PROVISION.getDiscovery);

        $("#discoveryTable").on("click", ".provisionButton", PROVISION.monitorDevice);
    },

    loadDiscoveryTable: function (isUpdate = false) {

        $.get(
            "getDiscoveryDevices",

            function (data) {

                var dataSet = data.result.list

                if (isUpdate) $('#discoveryTable').DataTable().destroy();

                $('#discoveryTable').html(`<table id="discoveryTable" class="table table-hover table-bordered display"></table>`)

                $('#discoveryTable').DataTable({

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

    modelForm: function () {

        COMPONENTS.modal("Add device", "Add", "DISCOVERY.addDeviceAction")

        $("#modalBody").html(`<form id="discoveryForm"><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="btnradio1" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="btnradio1">Ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="btnradio2" autocomplete="off"><label class="btn btn-outline-primary" for="btnradio2">ssh</label></div><div id="deviceCredForm"></div></form>`);

        $('input[type="radio"]').click(function () {
            if ($(this).attr("value") === "ping") {
                $("#deviceCredForm").html(`<div class="form-floating mt-3"><input type="text" autocomplete="off" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div>`)
            }
            if ($(this).attr("value") === "ssh") {
                $("#deviceCredForm").html(`<div class="form-floating mt-3"><input type="text" autocomplete="off" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="d-flex justify-content-between"><div class="form-floating mt-1 col-5"><input type="text" autocomplete="off" class="form-control" id="username" name="username" placeholder="Username" autocomplete="off"><label for="floatingInput">Username</label><div class="invalid-feedback">Invalid Username</div></div><div class="form-floating mt-1 col-7 ms-1"><input type="password" autocomplete="off" class="form-control" id="password" name="password" placeholder="Password" autocomplete="off"><label for="floatingInput">Password</label><div class="invalid-feedback">Invalid Password</div></div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" id="ip" placeholder="IP" name="ip" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div>`)
            }
        });

        $('#btnradio1').trigger('click');

        $('#modal').modal('toggle');

    },

    addDeviceAction: function () {

        let doSubmit = true;

        $.validator.addMethod('IP4Checker', function(value) {
            var expression = /^(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            return value.match(expression);
        }, 'Invalid IP address');

        $('#discoveryForm').validate({
            rules: {
                'deviceName': {
                    required:true,
                    maxlength: 20
                },
                'ip': {
                    required:true,
                    IP4Checker: true
                },
                'username': {
                    required:true,
                    maxlength: 20
                },
                'password': {
                    required:true,
                    maxlength: 20
                },
            },
            highlight: function (element) {
                $(element).addClass( "is-invalid" )
            },
            unhighlight: function (element) {
                $(element).removeClass( "is-invalid" );
            },
            errorPlacement: function(error, element) {
                $(element).parent().find(".invalid-feedback").html(error);
            },
        });

        if(!$('#discoveryForm').valid()) return

        if (doSubmit) {

            if ($("#id").val() === undefined) {

                if ($('input[name="type"]:checked').val() === "ping") {

                    $.post(
                        "addDiscoveryDevice",

                        $('#discoveryForm').serialize(),

                        function (data) {

                            $('#modal').modal('toggle');

                            COMPONENTS.alert("Device Added", data.result.status, data.result.code === 1 ? "success" : "danger")

                            DISCOVERY.loadDiscoveryTable(true);

                        }
                    )

                } else {

                    $.post(
                        "addDiscoveryDevice",

                        $('#discoveryForm').serialize(),

                        function (data) {

                            $('#modal').modal('toggle');

                            COMPONENTS.alert("Device Added", data.result.status, data.result.code === 1 ? "success" : "danger")

                            DISCOVERY.loadDiscoveryTable(true);

                        }
                    )

                }

            } else {

                if ($('input[name="type"]:checked').val() === "ping") {

                    $.post(
                        "updateDiscoveryDevice",

                        $('#discoveryForm').serialize(),

                        function (data) {

                            $('#modal').modal('toggle');

                            COMPONENTS.alert("Device Added", data.result.status, data.result.code === 1 ? "success" : "danger")

                            DISCOVERY.loadDiscoveryTable(true);

                        }
                    )

                } else {

                    $.post(
                        "updateDiscoveryDevice",

                        $('#discoveryForm').serialize(),

                        function (data) {

                            $('#modal').modal('toggle');

                            COMPONENTS.alert("Device Added", data.result.status, data.result.code === 1 ? "success" : "danger")

                            DISCOVERY.loadDiscoveryTable(true);

                        }
                    )

                }

            }

        }

    },

    deleteDeviceAction: function () {

        $.post(
            "deleteDiscoveryDevice",

            {id:$(this).data("id")},

            function () {

                COMPONENTS.alert("Device Delete", "Device has been deleted", "danger")

                DISCOVERY.loadDiscoveryTable(true);

            }
        )

    },

    getDeviceData: function (id) {

        $.post(
            "getSingleDiscoveryDevice",

            {id: $(this).data("id")},

            function (data) {

                data = data.result.data

                COMPONENTS.modal("Update device", "Update", "DISCOVERY.addDeviceAction")

                if (data.type === "ping") {
                    $("#modalBody").html(`<form id="discoveryForm"><div id="deviceCredForm"><input type="hidden" autocomplete="off" value="${data.id}" class="form-control" id="id" name="id" placeholder="id" ><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="btnradio1" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="btnradio1">Ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="btnradio2" autocomplete="off" disabled="disabled"><label class="btn btn-outline-primary" for="btnradio2">ssh</label></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.deviceName}" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.ip}" class="form-control" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div></div></form>`)
                } else {
                    $("#modalBody").html(`<form id="discoveryForm"><div id="deviceCredForm"><input type="hidden" autocomplete="off" value="${data.id}" class="form-control" id="id" name="id" placeholder="id" ><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="btnradio1" autocomplete="off" disabled="disabled"><label class="btn btn-outline-primary" for="btnradio1">Ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="btnradio2" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="btnradio2">ssh</label></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.deviceName}" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="d-flex justify-content-between"><div class="form-floating mt-1 col-5"><input type="text" autocomplete="off" value="${data.username}" class="form-control" id="username" name="username" placeholder="Username" autocomplete="off"><label for="floatingInput">Username</label><div class="invalid-feedback">Invalid Username</div></div><div class="form-floating mt-1 col-7 ms-1"><input type="password" autocomplete="off" class="form-control" id="password" name="password" placeholder="Password" autocomplete="off"><label for="floatingInput">Password</label><div class="invalid-feedback">Invalid Password</div></div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" value="${data.ip}" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div></div></form>`)
                }

                $('input[type="radio"]').trigger('click');

                $('#modal').modal('toggle');

            }
        )
    }
}

var PROVISION = {

    getDiscovery: function () {

        $.post(
            "checkProvision",

            {id: $(this).data("id"), type: $(this).data("type"), ip: $(this).data("ip")},

            function (data) {

                COMPONENTS.alert(
                    "Discovery Result",

                    data.result.status,

                    data.result.code === 1 ? "success" : "danger"
                );

                if (data.result.code === 1) {

                    DISCOVERY.loadDiscoveryTable(true)

                }

            }
        )

    },

    monitorDevice: function () {
        $.post(
            "putProvision",

            {id: $(this).data("id"), type: $(this).data("type"), ip: $(this).data("ip")},

            function (data) {

                COMPONENTS.alert(
                    "Discovery Result",

                    data.result.status,

                    data.result.code === 1 ? "success" : "danger"
                );

                if (data.result.code === 1) {

                    DISCOVERY.loadDiscoveryTable(true)

                }

            }
        )
    }

}