var MONITOR = {
    loadMonitor: function () {
        var html = `
                <div class="d-flex">
                <div class="m-3 card border shadow" id="sideNav" style="min-width: 400px;">
                    <div class="card-header pb-0">
                        <div class="h3">
                            Monitor
                        </div>
                    </div>
                    <div class="card-body" style="height: 70vh; overflow: auto;" id="monitorList">
                    
                    </div>
                </div>
                <div class="m-3 container" >
                    <div class="row" id="monitorHeader">
                        <h2>
                            Please select any moniter
                        </h2>
                    </div>
                    <hr>
                    <div class="row" id="monitorContent" >
                    </div>
                </div>
            </div>
        `

        $('#body').removeClass()
        $('#body').html(html)

        MONITOR.loadMonitorDevices()
    },
    loadMonitorDevices: function (){
        $.get(
            "getMonitorDevices",
            function (data){
                $('#monitorList').html(data.result.result)
            }
        )
    }
}