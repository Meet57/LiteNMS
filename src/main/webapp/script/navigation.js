var navigation = {

    showDashboard: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#dashboardNav').addClass("active")

        dashboard.loadDashboard()

        localStorage.setItem("navigation", "showDashboard")

    },

    showDiscovery: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#discoveryNav').addClass("active")

        discovery.loadDiscovery()

        localStorage.setItem("navigation", "showDiscovery")

    },

    showMonitor: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#monitorNav').addClass("active")

        monitor.loadMonitor()

        localStorage.setItem("navigation", "showMonitor")

    },

    loadPage: function () {

        var tab = localStorage.getItem("navigation")

        $("#navUsername").html(localStorage.getItem("username"))

        if (tab === "logout") window.location.reload();

        if (tab) {

            navigation[tab]()

        } else {

            navigation.showDashboard()

        }

    },

    logout: function () {

        let request = {
            
            url: "logout",

            callback: () => {
                
                localStorage.setItem("navigation", "logout")

                localStorage.removeItem("username")

                window.location.replace('login')

            },

        };

        api.ajaxpost(request);

    }
    
}