var COMPONENTS = {
    card: function (title, body, width = '10rem') {
        return `
            <div class="col-auto card my-2 mx-1 shadow p-0 h-100" style="min-width: ${width};">
            <div class="card-header h3">
                    ${title}
            </div>
            <div class="card-body p-3">
                <div class="card-text">
                    ${body}
                </div>
            </div>
        `
    },
    table: function (title, content) {

        var table = `
        <table class="w-100 table table-striped table-hover mt-3"><thead class="table-dark"><tr>
        `
        title.forEach(title => {
            table += `
                <td>${title}</td>
            `
        });

        table += `</tr></thead><tbody>`

        content.forEach(row => {
            table += '<tr>'
            row.forEach(data => {
                table += `<td>${data}</td>`
            })
            table += '</tr>'
        })

        table += '</tbody></table>'

        return table
    },
    modal: function (id, header, buttonName, buttonFunction) {
        var modal = `
        <!-- Modal -->
        <div class="modal fade" id="${id}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">${header}</h5>
              </div>
              <div class="modal-body" id="${id}Body">
                ...
              </div>
              <div class="modal-footer" id="${id}Footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="${buttonFunction}">${buttonName}</button>
              </div>
            </div>
          </div>
        </div>
        `
        return modal
    },
    alert: function (title, body, type="primary") {
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
        $(document).ready(function(){
            $("#liveToast").toast("show");
        });
    }
}