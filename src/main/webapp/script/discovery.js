var DISCOVERY = {

    loadDiscovery: function () {

        $("#body").removeClass()

        $("#body").html(`<div class="container"><div class="row"><div class="col-9 mx-auto mt-2 py-3"><button type="button" class="btn btn-primary" onclick="DISCOVERY.modelForm()">Add Device</button></div></div><div class="row"><div class="col-9 mx-auto bg-light shadow mt-2 p-3 rounded"><table id="discoveryTable" class="table table-hover table-bordered display"></table></div></div></div>`)

        $("#discoveryTable").on("click", ".deleteButton", DISCOVERY.deleteDeviceModal);

        $("#discoveryTable").on("click", ".editButton", DISCOVERY.getDeviceData);

        $("#discoveryTable").on("click", ".runButton", DISCOVERY.getDiscovery);

        $("#discoveryTable").on("click", ".provisionButton", DISCOVERY.monitorDevice);

        let request = {

            url: "getDiscoveryDevices",

            data: {},

            callback: DISCOVERY.loadDiscoveryTable,

        };

        API.ajaxpost(request);

    },

    loadDiscoveryTable: function (data, isUpdate = false) {

        if (isUpdate) {

            let request = {

                url: "getDiscoveryDevices",

                data: {},

                callback: DISCOVERY.loadDiscoveryTable,

            };

            $('#discoveryTable').DataTable().destroy()

            API.ajaxpost(request)

            return

        }

        var dataSet = data.result.list

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

    },

    modelForm: function () {

        COMPONENTS.modal("Add device", "Add", "DISCOVERY.addDeviceAction")

        $("#modalBody").html(`<form id="discoveryForm"><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="pingRadio" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="pingRadio">ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="sshRadio" autocomplete="off"><label class="btn btn-outline-primary" for="sshRadio">ssh</label></div><div id="deviceCredForm"></div></form>`);


        $('input[type="radio"]').click(function () {

            if ($(this).attr("value") === "ping") {

                $("#deviceCredForm").html(`<div class="form-floating mt-3"><input type="text" autocomplete="off" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div>`)

            }

            if ($(this).attr("value") === "ssh") {

                $("#deviceCredForm").html(`<div class="form-floating mt-3"><input type="text" autocomplete="off" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="d-flex justify-content-between"><div class="form-floating mt-1 col-5"><input type="text" autocomplete="off" class="form-control" id="username" name="username" placeholder="Username" autocomplete="off"><label for="floatingInput">Username</label><div class="invalid-feedback">Invalid Username</div></div><div class="form-floating mt-1 col-7 ms-1"><input type="password" autocomplete="off" class="form-control" id="password" name="password" placeholder="Password" autocomplete="off"><label for="floatingInput">Password</label><div class="invalid-feedback">Invalid Password</div></div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" id="ip" placeholder="IP" name="ip" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div>`)

            }

        });

        $('#pingRadio').trigger('click');

        $('#modal').modal('toggle');

    },

    addDeviceAction: function () {

        let rules = {
            'deviceName': {
                required: true,
                maxlength: 20
            },
            'ip': {
                required: true,
                IP4Checker: true
            },
            'username': {
                required: true,
                maxlength: 20
            },
            'password': {
                required: true,
                maxlength: 20
            },
        }

        validator.formValidator("#discoveryForm", rules)

        if (!$('#discoveryForm').valid()) return

        if ($("#id").val() === undefined) {

            let request = {

                url: "addDiscoveryDevice",

                data: $('#discoveryForm').serialize(),

                callback: DISCOVERY.sendNotification,

            };

            API.ajaxpost(request, false)

            $('#modal').modal('toggle');

            DISCOVERY.loadDiscoveryTable(null, true);

        } else {

            let request = {

                url: "updateDiscoveryDevice",

                data: $('#discoveryForm').serialize(),

                callback: DISCOVERY.sendNotification,

            };

            API.ajaxpost(request, false)

            $('#modal').modal('toggle');

            DISCOVERY.loadDiscoveryTable(null, true);

        }


    },

    deleteDeviceModal: function () {

        COMPONENTS.modal("Delete device", "Delete", "DISCOVERY.deleteDeviceAction", $(this).data("id"))

        $('#modalBody').html("Do you want to delete this device ?");

        $('#modal').modal('toggle');

    },

    deleteDeviceAction: function (id) {

        $('#modal').modal('toggle');

        let request = {

            url: "deleteDiscoveryDevice",

            data: {id},

            callback: DISCOVERY.sendNotification,

        };

        API.ajaxget(request, true, true);

    },

    getDeviceData: function (id, data) {

        let request = {

            url: "getSingleDiscoveryDevice",

            data: {id: $(this).data("id")},

            callback: DISCOVERY.openDeviceEditForm,

        };

        API.ajaxget(request);

    },

    openDeviceEditForm: function (data) {

        data = data.result.data

        COMPONENTS.modal("Update device", "Update", "DISCOVERY.addDeviceAction")

        if (data.type === "ping") {

            $("#modalBody").html(`<form id="discoveryForm"><div id="deviceCredForm"><input type="hidden" autocomplete="off" value="${data.id}" class="form-control" id="id" name="id" placeholder="id" ><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="pingRadio" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="pingRadio">ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="sshRadio" autocomplete="off" disabled="disabled"><label class="btn btn-outline-primary" for="sshRadio">ssh</label></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.deviceName}" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.ip}" class="form-control" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div></div></form>`)

        } else {

            $("#modalBody").html(`<form id="discoveryForm"><div id="deviceCredForm"><input type="hidden" autocomplete="off" value="${data.id}" class="form-control" id="id" name="id" placeholder="id" ><div class="btn-group" role="group" aria-label="Basic radio toggle button group"><input type="radio" class="btn-check" name="type" value="ping" id="pingRadio" autocomplete="off" disabled="disabled"><label class="btn btn-outline-primary" for="pingRadio">ping</label><input type="radio" class="btn-check" name="type" value="ssh" id="sshRadio" autocomplete="off" checked="checked"><label class="btn btn-outline-primary" for="sshRadio">ssh</label></div><div class="form-floating mt-1"><input type="text" autocomplete="off" value="${data.deviceName}" class="form-control" id="deviceName" name="deviceName" placeholder="Device Name" ><label for="floatingInput">Device Name</label><div class="invalid-feedback">Invalid Device Name</div></div><div class="d-flex justify-content-between"><div class="form-floating mt-1 col-5"><input type="text" autocomplete="off" value="${data.username}" class="form-control" id="username" name="username" placeholder="Username" autocomplete="off"><label for="floatingInput">Username</label><div class="invalid-feedback">Invalid Username</div></div><div class="form-floating mt-1 col-7 ms-1"><input type="password" autocomplete="off" class="form-control" id="password" name="password" placeholder="Password" autocomplete="off"><label for="floatingInput">Password</label><div class="invalid-feedback">Invalid Password</div></div></div><div class="form-floating mt-1"><input type="text" autocomplete="off" class="form-control" value="${data.ip}" id="ip" name="ip" placeholder="IP" ><label for="floatingInput">IP</label><div class="invalid-feedback">Invalid IP</div></div></div></form>`)

        }

        $('input[type="radio"]').trigger('click');

        $('#modal').modal('toggle');

    },

    getDiscovery: function () {

        let request = {

            url: "checkProvision",

            data: {

                id: $(this).data("id"),

                type: $(this).data("type"),

                ip: $(this).data("ip"),

            },

            callback: DISCOVERY.sendNotification,

        };

        API.ajaxget(request, true, true)

    },

    monitorDevice: function () {

        let request = {

            url: "putProvision",

            data: {
                id: $(this).data("id"),
                type: $(this).data("type"),
                ip: $(this).data("ip"),
            },

            callback: DISCOVERY.sendNotification,

        };

        API.ajaxget(request)

    },

    sendNotification: function (data, updateTable = false) {

        COMPONENTS.alert("Discovery Tab", data.result.status, data.result.code);

        if (updateTable) {

            DISCOVERY.loadDiscoveryTable(null, true)

        }

    }

}