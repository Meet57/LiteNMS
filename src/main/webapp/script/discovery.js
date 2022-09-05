var DISCOVERY = {
    loadDiscovery: function () {
        $("#body").removeClass()

        $("#body").html(`
            <div class="container">
                <div class="row">
                    <div class="col-9 mx-auto mt-2 py-3">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#discoveryModalForm">
                        Add Device
                  </button>
                    </div>
                </div>
                <div class="row">
                    <div class="col-9 mx-auto bg-light shadow mt-2 p-3 rounded">
                        <table id="discoveryTable" class="table table-hover table-bordered display" width="100%"></table>
                    </div>
                </div>
            </div>
        `)

        DISCOVERY.loadDiscoveryTable()
        DISCOVERY.loadDiscoveryForm()
    },
    loadDiscoveryTable: function (isUpdate = false) {
        $.get(
            "getDiscoveryDevices",
            function (data,status){
                var dataSet = data.result.result
                    if(isUpdate){
                        $('#discoveryTable').DataTable().destroy();
                        $('#discoveryTable').DataTable({
                            data: dataSet,
                            columns: [
                                { title: 'id' },
                                { title: 'Device Name' },
                                { title: 'IP' },
                                { title: 'ACTIONS' }
                            ],
                        });
                    }else{
                        $('#discoveryTable').DataTable({
                            data: dataSet,
                            columns: [
                                { title: 'id' },
                                { title: 'Device Name' },
                                { title: 'IP' },
                                { title: 'ACTIONS' }
                            ],
                        });
                    }
            }
        )
    },
    loadDiscoveryForm: function () {
        $("#body").append(COMPONENTS.modal("discoveryModalForm", "Add Device", "Add", "DISCOVERY.addDeviceAction()"))
        $("#body").append(COMPONENTS.modal("discoveryModalFormForEdit", "Update Device", "Update", "DISCOVERY.updateDeviceAction()"))
        $("#discoveryModalFormBody").html(DISCOVERY.addDeviceForm());

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
            }
            else if (this.value == 'SSH') {
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
    updateDeviceForm:function (deviceType,id,deviceName,ip,username,password){
        if(deviceType == "ping"){
            var html = `
                <form>
                    <input type="radio" class="btn-check" name="deviceType" value="ping" id="btnradio1" autocomplete="off" checked>
                    <label class="btn btn-outline-primary" for="btnradio1">Ping</label>
                    <div class="form-floating mt-3">
                        <input type="text" value="${id}" class="form-control" id="id" placeholder="ID" readonly>
                        <label for="id">ID</label>
                    </div>
                    <div class="form-floating mt-3">
                        <input type="text" autocomplete="off" value="${deviceName}" class="form-control" id="name" placeholder="Device Name" required>
                        <label for="floatingInput">Device Name</label>
                        <div class="invalid-feedback">Invalid Device Name</div>
                    </div>
                    <div class="form-floating mt-1">
                        <input type="text" autocomplete="off" value="${ip}" class="form-control" id="ip" placeholder="IP" required>
                        <label for="floatingInput">IP</label>
                        <div class="invalid-feedback">Invalid IP</div>
                    </div>
                </form>
            `
            $("#discoveryModalFormForEditBody").html(html);
            $('#discoveryModalFormForEdit').modal('toggle');
        }

    },
    addDeviceForm: function () {
        var html = `
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
        return html
    },
    addDeviceAction: function () {
        var doSubmit = true
        $("input").removeClass("is-invalid");

        var ipAddress = $("#ip");
        var expression = /((^\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\s*$)|(^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$))/;
        if (!expression.test(ipAddress.val())) {
            ipAddress.addClass("is-invalid")
            doSubmit = false
        }

        if ($('input[name="deviceType"]:checked').val() == "ping") {
            if ($("#name").val().length == 0) {
                $("#name").addClass("is-invalid")
                doSubmit = false
            }
        } else {
            if ($("#name").val().length == 0) {
                $("#name").addClass("is-invalid")
                doSubmit = false
            }
            if ($("#username").val().length == 0) {
                $("#username").addClass("is-invalid")
                doSubmit = false
            }
            if ($("#password").val().length == 0) {
                $("#password").addClass("is-invalid")
                doSubmit = false
            }
        }

        if (doSubmit) {
            if($('input[name="deviceType"]:checked').val() == "ping"){
                $.post(
                    "addDiscoveryDevice",
                    {deviceName: $("#name").val(),ip:$("#ip").val()},
                    function (data,status) {
                        $('#discoveryModalForm').modal('toggle');
                        COMPONENTS.alert("Device Added","Device has been Added","success")
                        DISCOVERY.loadDiscoveryTable(true);
                    }
                )
            }
        }
    },
    updateDeviceAction:function (){
        console.log("Hello")
    },
    deleteDeviceAction: function (id,name){
        $.post(
            "deleteDiscoveryDevice",
            {id},
            function (data,status) {
                COMPONENTS.alert("Device Delete","Device has been deleted","danger")
                DISCOVERY.loadDiscoveryTable(true);
            }
        )
    }
}