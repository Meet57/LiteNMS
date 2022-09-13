var API = {

    ajaxpost: function (request) {
        $.ajax({

            type: 'POST',

            url: request.url,

            data: request.data,

            success: function (data) {
                let callbacks;

                if (request.callback !== undefined) {
                    callbacks = $.Callbacks();

                    callbacks.add(request.callback);

                    callbacks.fire(data);

                    callbacks.remove(request.callback);
                }
            },
            error: function () {
                COMPONENTS.alert("Error", "Some error occurred while calling API", "danger")
            },
            timeout: 15000
        });
    },

    ajaxget: function (request) {
        $.ajax({

            type: 'GET',

            url: request.url,

            data: request.data,

            success: function (data) {
                let callbacks;

                if (request.callback !== undefined) {
                    callbacks = $.Callbacks();

                    callbacks.add(request.callback);

                    callbacks.fire(data);

                    callbacks.remove(request.callback);
                }
            },
            error: function () {
                COMPONENTS.alert("Error", "Some error occurred while calling API", "danger")
            },
            timeout: 15000
        });
    }
};