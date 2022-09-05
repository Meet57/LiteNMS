var NAVIGATION = {
    showDashboard: function(loadContent = true){
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#dashboardNav').addClass("active")
        if(loadContent){
            DASHBOARD.loadDashboard()
        }
        localStorage.setItem("navigation","showDashboard")
    },
    showDiscovery: function(loadContent = true){
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#discoveryNav').addClass("active")
        if(loadContent){
            DISCOVERY.loadDiscovery()
        }
        localStorage.setItem("navigation","showDiscovery")
    },
    showMoniter: function(loadContent = true){
        $('#navbarSupportedContent ul a').filter(".active").removeClass("active")
        $('#moniterNav').addClass("active")
        if(loadContent){
            MONITER.loadMoniter()
        }
        localStorage.setItem("navigation","showMoniter")
    },
    loadPage: function(){
        var tab = localStorage.getItem("navigation")
        if(tab){
            NAVIGATION[tab]()
        }else{
            NAVIGATION.showDashboard()
        }
    }
}