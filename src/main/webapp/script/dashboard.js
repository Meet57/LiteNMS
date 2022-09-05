var DASHBOARD = {
    loadDashboard: function () {
        $("#body").addClass("p-2 d-flex flex-wrap")
        $("#body").html(COMPONENTS.card("Meet", "Hello World"))
        $("#body").append(COMPONENTS.card("Shekhar", "Hello World 2","20%"))
        $("#body").append(COMPONENTS.card("Rahil", "Hello World 3"))
        $("#body").append(COMPONENTS.card(
            "Table",
            COMPONENTS.table(
                [1, 2, 3, 4, 5, 6, 7, 8, 9],
                [
                    [1, 2, 3, 4, 5, 6, 7, 8, 9],
                    [5, 6, 7, 8],
                ]
            ),
            "500px"
        ))
    }
}