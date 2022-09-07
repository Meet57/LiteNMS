var MONITER = {
    loadMonitor: function () {
        var html = `
                <div class="d-flex">
                <div class="m-3 card border shadow" id="sideNav" style="min-width: 300px;">
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
                    </div>
                    <hr>
                    <div class="row" id="monitorContent" >
                    </div>
                </div>
            </div>
        `

        $('#body').removeClass()
        $('#body').html(html)
        $('#monitorHeader').html("<h2>10.20.40.226</h2>")
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#monitorContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#monitorList').append(MONITER.addMonitor('10.20.40.226'))
        $('#monitorList').append(MONITER.addMonitor('10.20.42.142'))
        $('#monitorList').append(MONITER.addMonitor('10.20.42.142'))
        $('#monitorList').append(MONITER.addMonitor('10.20.40.220'))
    },

    addMonitor: function(ip){
        return `
            <div class="border rounded border-1 my-2 py-2 d-flex justify-content-around align-items-center">
                ${ip}
                <div>
                    <button class="btn btn-outline-primary btn-sm">View</button>
                    <button class="btn btn-outline-danger btn-sm">Delete</button>
                </div>
            </div>
        `
    }
}