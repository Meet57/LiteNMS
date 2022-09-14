var AUTHENTICATION = {
    load:function (){
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
        $("#loginForm").submit(function (event) {
            event.preventDefault()
            if ($("#loginForm").valid()) {

                let request = {
                    url: "authenticate",
                    data: $("#loginForm").serialize(),
                    callback: AUTHENTICATION.actions,
                };

                API.ajaxpost(request);
            }
        })
    },
    actions: function (data) {
        if(data.result.code === 1){
            localStorage.setItem("username",data.username)
            localStorage.removeItem("navigation")
            window.location.href = '/';
        }else{
            COMPONENTS.alert("Authentication", data.result.status, "danger")
        }
    },
}