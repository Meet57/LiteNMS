var DISCOVERY = {
    loadDiscovery: function () {
        $("#body").removeClass()

        $("#body").html(`
            <div class="container">
                <div class="modal fade" id="modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"><div class="modal-dialog modal-dialog-scrollable"><div class="modal-content"><div class="modal-header"><h5 class="modal-title" id="modalHeading"></h5></div><div class="modal-body" id="modalBody">...</div><div class="modal-footer" id="modalFooter"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button><button type="button" class="btn btn-primary" id="modalSubmitButton" onclick="DISCOVERY.addDeviceAction()"></button></div></div></div></div>
                <div class="row">
                    <div class="col-9 mx-auto mt-2 py-3">
                    <button type="button" class="btn btn-primary" onclick="DISCOVERY.createNewDevice()">
                        Add Device
                  </button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-9 mx-auto bg-light shadow mt-2 p-3 rounded">
                        <table id="discoveryTable" class="table table-hover table-bordered display"></table>
                    </div>
                </div>
            </div>
        `)

        DISCOVERY.loadDiscoveryTable()
    },
    createNewDevice: function () {
        COMPONENTS.modal("Add Device", "Add", "DISCOVERY.addDeviceAction")
        $("#modalBody").html(DISCOVERY.addDeviceForm());
        DISCOVERY.loadDiscoveryForm()
        $('#modal').modal('toggle');
    },
    loadDiscoveryTable: function (isUpdate = false) {
        $.get(
            "getDiscoveryDevices",
            function (data) {
                var dataSet = data.result.result
                if (isUpdate) {
                    $('#discoveryTable').DataTable().destroy();
                    $('#discoveryTable').DataTable({
                        data: dataSet,
                        columns: [
                            {title: 'id'},
                            {title: 'Device Name'},
                            {title: 'IP'},
                            {title: 'TYPE'},
                            {title: 'ACTIONS'}
                        ],
                    });
                } else {
                    $('#discoveryTable').DataTable({
                        data: dataSet,
                        columns: [
                            {title: 'id'},
                            {title: 'Device Name'},
                            {title: 'IP'},
                            {title: 'TYPE'},
                            {title: 'ACTIONS'}
                        ],
                    });
                }
            }
        )
    },
    loadDiscoveryForm: function () {
        $("#modalBody").html(DISCOVERY.addDeviceForm());

        $('input[type=radio][name=deviceType]').change(function () {
            if (this.value === 'ping') {
                $("#deviceCredForm").html(`
                    <div class="form-floating mt-3">
                        <input type="text" autocomplete="off" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" class="form-control" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                `)
            } else if (this.value === 'SSH') {
                $("#deviceCredForm").html(`
                    <div class="form-floating mt-3">
                        <input type="text" autocomplete="off" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="d-flex justify-content-between">
                        <div class="form-floating mt-1 col-5">
                            <input type="text" autocomplete="off" class="form-control" id="username" placeholder="Username" autocomplete="off">
                            <label for="floatingInput">Username</label>
                            <div class="invalid-feedback">Invalid Username</div>
                        </div>
                        <div class="form-floating mt-1 col-7 ms-1">
                            <input type="password" autocomplete="off" class="form-control" id="password" placeholder="Password" autocomplete="off">
                            <label for="floatingInput">Password</label>
                            <div class="invalid-feedback">Invalid Password</div>
                        </div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" class="form-control" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                `)
            }
        })
    },
    addDeviceForm: function () {
        return `
            <form>
                <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                    <input type="radio" class="btn-check" name="deviceType" value="ping" id="btnradio1" autocomplete="off" checked>
                    <label class="btn btn-outline-primary" for="btnradio1">Ping</label>
                
                    <input type="radio" class="btn-check" name="deviceType" value="SSH" id="btnradio2" autocomplete="off">
                    <label class="btn btn-outline-primary" for="btnradio2">SSH</label>
                </div>
                <div id="deviceCredForm">
                    <div class="form-floating mt-3">
                        <input type="text" autocomplete="off" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" class="form-control" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                </div>
            </form>
        `
    },
    updateDeviceForm(type, id, deviceName, ip, username) {
        COMPONENTS.modal("Update Device", "Update", "DISCOVERY.addDeviceAction")
        let html;
        if (type === "ping") {
            html = `
            <form>
                <div id="deviceCredForm">
                    <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                        <input type="radio" class="btn-check" name="deviceType" value="ping" id="btnradio1" autocomplete="off" checked>
                        <label class="btn btn-outline-primary" for="btnradio1">Ping</label>
                    
                        <input type="radio" class="btn-check" name="deviceType" value="SSH" id="btnradio2" autocomplete="off" disabled>
                        <label class="btn btn-outline-primary" for="btnradio2">SSH</label>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${id}" class="form-control" id="id" placeholder="id" readonly>
                        <label for="floatingInput">ID</label>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${deviceName}" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${ip}" class="form-control" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                </div>
            </form>
            `
            $("#modalBody").html(html)
        } else {
            html = `
            <form>
                <div id="deviceCredForm">
                    <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                        <input type="radio" class="btn-check" name="deviceType" value="ping" id="btnradio1" autocomplete="off" disabled>
                        <label class="btn btn-outline-primary" for="btnradio1">Ping</label>
                    
                        <input type="radio" class="btn-check" name="deviceType" value="SSH" id="btnradio2" autocomplete="off" checked>
                        <label class="btn btn-outline-primary" for="btnradio2">SSH</label>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${id}" class="form-control" id="id" placeholder="id" readonly>
                        <label for="floatingInput">ID</label>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${type}" class="form-control" id="type" placeholder="type" readonly>
                        <label for="floatingInput">Type</label>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${deviceName}" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="d-flex justify-content-between">
                        <div class="form-floating mt-1 col-5">
                            <input type="text" autocomplete="off" value="${username}" class="form-control" id="username" placeholder="Username" autocomplete="off">
                            <label for="floatingInput">Username</label>
                            <div class="invalid-feedback">Invalid Username</div>
                        </div>
                        <div class="form-floating mt-1 col-7 ms-1">
                            <input type="password" autocomplete="off" class="form-control" id="password" placeholder="Password" autocomplete="off">
                            <label for="floatingInput">Password</label>
                            <div class="invalid-feedback">Invalid Password</div>
                        </div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" class="form-control" value="${ip}" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                </div>
            </form>
            `
            $("#modalBody").html(html)
        }
        $('#modal').modal('toggle');
    },
    addDeviceAction: function () {
        let doSubmit = true;
        $("input").removeClass("is-invalid");

        let ipAddress = $("#ip");
        const ipRegex = /((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/;
        const htmlRegex = /<(“[^”]*”|'[^’]*’|[^'”>])*>/;

        if (!ipRegex.test(ipAddress.val())) {
            ipAddress.addClass("is-invalid")
            doSubmit = false
        }

        if ($('input[name="deviceType"]:checked').val() === "ping") {
            if ($("#name").val().trim().length === 0) {
                $("#name").addClass("is-invalid")
                doSubmit = false
            }
        } else {
            if ($("#name").val().trim().length === 0) {
                $("#name").addClass("is-invalid")
                doSubmit = false
            }
            if ($("#username").val().trim().length === 0) {
                $("#username").addClass("is-invalid")
                doSubmit = false
            }
            if ($("#password").val().trim().length === 0) {
                $("#password").addClass("is-invalid")
                doSubmit = false
            }
        }

        if (doSubmit) {
            if ($("#id").val() === undefined) {
                if ($('input[name="deviceType"]:checked').val() === "ping") {
                    $.post(
                        "addDiscoveryDevice",
                        {deviceName: $("#name").val().trim(), ip: $("#ip").val().trim(), type: "ping"},
                        function () {
                            $('#modal').modal('toggle');
                            COMPONENTS.alert("Device Added", "Device has been Added", "success")
                            DISCOVERY.loadDiscoveryTable(true);
                        }
                    )
                } else {
                    $.post(
                        "addDiscoveryDevice",
                        {
                            deviceName: $("#name").val().trim(),
                            ip: $("#ip").val().trim(),
                            type: "ssh",
                            username: $("#username").val().trim(),
                            password: $("#password").val().trim()
                        },
                        function () {
                            $('#modal').modal('toggle');
                            COMPONENTS.alert("Device Added", "Device has been Added", "success")
                            DISCOVERY.loadDiscoveryTable(true);
                        }
                    )
                }
            } else {
                console.log($('input[name="deviceType"]:checked').val() === "ping");
                if ($('input[name="deviceType"]:checked').val() === "ping") {
                    $.post(
                        "updateDiscoveryDevice",
                        {
                            id: $("#id").val().trim(),
                            type: "ping",
                            deviceName: $("#name").val().trim(),
                            ip: $("#ip").val().trim()
                        },
                        function () {
                            $('#modal').modal('toggle');
                            COMPONENTS.alert("Device Updated", "Device has been Updated", "success")
                            DISCOVERY.loadDiscoveryTable(true);
                        }
                    )
                } else {
                    $.post(
                        "updateDiscoveryDevice",
                        {
                            id: $("#id").val().trim(),
                            type: "ssh",
                            deviceName: $("#name").val().trim(),
                            ip: $("#ip").val().trim(),
                            username: $("#username").val().trim(),
                            password: $("#password").val().trim()
                        },
                        function () {
                            $('#modal').modal('toggle');
                            COMPONENTS.alert("Device Updated", "Device has been Updated", "success")
                            DISCOVERY.loadDiscoveryTable(true);
                        }
                    )
                }
            }
        }
    },
    deleteDeviceAction: function (id) {
        $.post(
            "deleteDiscoveryDevice",
            {id},
            function () {
                COMPONENTS.alert("Device Delete", "Device has been deleted", "danger")
                DISCOVERY.loadDiscoveryTable(true);
            }
        )
    }
}

var PROVISION = {
    getDiscovery: function (id) {
        $.post(
            "checkProvision",
            {id},
            function (data) {
                COMPONENTS.alert(
                    "Discovery Result",
                    data.result.status,
                    data.result.code === 1 ? "success" : "danger"
                );
            }
        )
    }
}