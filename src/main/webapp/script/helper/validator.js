var validator = {
    formValidator: function (formId,rules){
        $(formId).validate({
            rules: rules,
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

        $.validator.addMethod('IP4Checker', function (value) {
            var expression = /^(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-4]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
            return value.match(expression);
        }, 'Invalid IP address');
    }
}