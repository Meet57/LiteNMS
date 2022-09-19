var authentication = {

    load: function () {

        let rules = {

            'username': {

                required: true,

                maxlength: 20

            },

            'password': {

                required: true,

                maxlength: 20

            },

        }

        validator.formValidator("#loginForm",rules);

        $("#loginForm").submit(function (event) {

            event.preventDefault()

            if ($("#loginForm").valid()) {

                let request = {

                    url: "authenticate",

                    data: $("#loginForm").serialize(),

                    callback: authentication.actions,

                };

                api.ajaxpost(request);

            }

        })

    },

    actions: function (data) {

        if (data.result.code === 1) {

            localStorage.setItem("username", data.username)

            localStorage.removeItem("navigation")

            window.location.replace("/")

        } else {

            components.alert("Authentication", data.result.status, 0)

        }

    },

}