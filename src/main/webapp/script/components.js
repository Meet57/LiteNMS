var components = {

    card: function (title, body, col = "auto", height = "h-100") {

        return `

            <div class="col-${col} card my-2 shadow p-0 ${height}">

                <div class="card-header h3">

                        ${title}

                </div>

                <div class="card-body p-3">

                    <div class="card-text">

                        ${body}

                    </div>

                </div>
                
            </div>

        `

    },

    modal: function (header, buttonName, buttonFunction, parameter) {

        let modalButton = $("#modalSubmitButton");

        $("#modalHeading").html(header)

        modalButton.html(buttonName)

        if (!parameter) {

            modalButton.attr("onclick", buttonFunction + "()");

        } else {

            modalButton.attr("onclick", buttonFunction + "(" + parameter + ")");

        }

    },

    alert: function (title, body, type = 1) {

        switch (type) {

            case 1:

                type = "success"

                break

            case 0:

                type = "danger"

                break

            default:

                break

        }

        var html = `

            <div class="position-fixed top-0 end-0 p-3" style="z-index: 99999">

              <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true">
              
                <div class="toast-header bg-${type}">

                  <strong class="me-auto text-white">${title}</strong>

                  <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>

                </div>

                <div class="toast-body">

                  ${body}

                </div>

              </div>

            </div>

        `
        $("#alert").html(html)

        $("#liveToast").toast("show");


    }

}

