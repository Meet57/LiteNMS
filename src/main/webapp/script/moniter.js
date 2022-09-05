var MONITER = {
    loadMoniter: function () {
        var html = `
                <div class="d-flex">
                <div class="m-3 card border shadow" id="sideNav" style="min-width: 300px;">
                    <div class="card-header pb-0">
                        <div class="h3">
                            Moniter
                        </div>
                    </div>
                    <div class="card-body" style="height: 70vh; overflow: auto;" id="moniterList">
                    
                    </div>
                </div>
                <div class="m-3 container" >
                    <div class="row" id="moniterHeader">
                    </div>
                    <hr>
                    <div class="row" id="moniterContent" >
                    </div>
                </div>
            </div>
        `

        $('#body').removeClass()
        $('#body').html(html)
        $('#moniterHeader').html("<h2>10.20.40.226</h2>")
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","200px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","220px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","130px"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content"))
        $('#moniterContent').append(COMPONENTS.card('Meet', "Some Content","500px"))
        $('#moniterList').append(MONITER.addMoniter('10.20.40.226'))
        $('#moniterList').append(MONITER.addMoniter('10.20.42.142'))
        $('#moniterList').append(MONITER.addMoniter('10.20.42.142'))
        $('#moniterList').append(MONITER.addMoniter('10.20.40.220'))
    },

    addMoniter: function(ip){
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