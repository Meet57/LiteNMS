var AUTHENTICATION = {
    login: function () {
        $("#loginForm").submit(function (event) {
            event.preventDefault()
            if ($("#loginForm").valid()) {
                $.post(
                    "authenticate",
                    $("#loginForm").serialize(),
                    function (data) {
                        COMPONENTS.alert(
                            "Login Result",
                            data.result.status,
                            data.result.code === 1 ? "success" : "danger"
                        )
                    }
                )
            }
        })
    },
    forget: function () {
        if ($("#loginForm").valid()) {
            $.post(
                "forgetPassword",
                $("#loginForm").serialize(),
                function (data) {
                    COMPONENTS.alert(
                        "Forgot Password",
                        data.result.status,
                        data.result.code === 1 ? "success" : "danger"
                    )
                }
            )
        }
    }
}

AUTHENTICATION.login()

$("#loginForm").validate({
    rules: {
        'username': {
            required: true,
            maxlength: 20
        },
        'password': {
            required: true,
            maxlength: 20
        },
    },
    highlight: function (element) {
        $(element).addClass("is-invalid")
    },
    unhighlight: function (element) {
        $(element).removeClass("is-invalid");
    },
    errorPlacement: function (error, element) {
        $(element).parent().find(".invalid-feedback").html(error);
    },
})