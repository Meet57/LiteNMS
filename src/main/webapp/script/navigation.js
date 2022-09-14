var NAVIGATION = {
    showDashboard: function (loadContent = true) {
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#dashboardNav').addClass("active")
        if (loadContent) {
            DASHBOARD.loadDashboard()
        }
        localStorage.setItem("navigation", "showDashboard")
    },
    showDiscovery: function (loadContent = true) {
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#discoveryNav').addClass("active")
        if (loadContent) {
            DISCOVERY.loadDiscovery()
        }
        localStorage.setItem("navigation", "showDiscovery")
    },
    showMonitor: function (loadContent = true) {
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#monitorNav').addClass("active")
        if (loadContent) {
            MONITOR.loadMonitor()
        }
        localStorage.setItem("navigation", "showMonitor")
    },
    loadPage: function () {
        var tab = localStorage.getItem("navigation")
        console.log(localStorage.getItem("username"))
        $("#navUsername").html(localStorage.getItem("username"))
        if(tab === "logout") window.location.reload();
        if (tab) {
            NAVIGATION[tab]()
        } else {
            NAVIGATION.showDashboard()
        }
    },
    logout: function () {
        let request = {
            url: "logout",
            callback: ()=>{
                localStorage.setItem("navigation", "logout")
                window.location.href = 'login';
            },
        };

        API.ajaxpost(request);
    }
}