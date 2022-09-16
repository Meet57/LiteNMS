var NAVIGATION = {

    showDashboard: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#dashboardNav').addClass("active")

        DASHBOARD.loadDashboard()

        localStorage.setItem("navigation", "showDashboard")

    },

    showDiscovery: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#discoveryNav').addClass("active")

        DISCOVERY.loadDiscovery()

        localStorage.setItem("navigation", "showDiscovery")

    },

    showMonitor: function () {

        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")

        $('#monitorNav').addClass("active")

        MONITOR.loadMonitor()

        localStorage.setItem("navigation", "showMonitor")

    },

    loadPage: function () {

        var tab = localStorage.getItem("navigation")

        $("#navUsername").html(localStorage.getItem("username"))

        if (tab === "logout") window.location.reload();

        if (tab) {

            NAVIGATION[tab]()

        } else {

            NAVIGATION.showDashboard()

        }

    },

    logout: function () {

        let request = {
            
            url: "logout",

            callback: () => {
                
                localStorage.setItem("navigation", "logout")

                window.location.href = 'login';

            },

        };

        API.ajaxpost(request);

    }
    
}