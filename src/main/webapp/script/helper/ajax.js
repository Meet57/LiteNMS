var api = {

    ajaxpost: function (request,async = true,parameter) {

        $.ajax({

            type: 'POST',

            url: request.url,

            data: request.data,

            async: async,

            success: function (data) {
                let callbacks;

                if (request.callback !== undefined) {
                    callbacks = $.Callbacks();

                    callbacks.add(request.callback);

                    callbacks.fire(data,parameter);

                    callbacks.remove(request.callback);
                }
            },

            error: function () {

                components.alert("Error", "Some error occurred while calling API", "danger")

            },

            timeout: 15000

        });

    },

    ajaxget: function (request,async=true,parameter) {

        $.ajax({

            type: 'GET',

            url: request.url,

            data: request.data,

            async: async,

            success: function (data) {

                let callbacks;

                if (request.callback !== undefined) {

                    callbacks = $.Callbacks();

                    callbacks.add(request.callback);

                    callbacks.fire(data,parameter);

                    callbacks.remove(request.callback);

                }

            },

            error: function () {

                components.alert("Error", "Some error occurred while calling API", "danger")

            },

            timeout: 15000

        });

    }

};