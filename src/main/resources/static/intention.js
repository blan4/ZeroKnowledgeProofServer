(function() {
    'use strict';

    var listenInterval = null;

    if (document.readyState != 'loading'){
        ready();
    } else {
        document.addEventListener('DOMContentLoaded', ready);
    }

    function ready() {
        var links = document.querySelectorAll('.intent');
        for (var i = 0; i < links.length; ++i) {
            links[i].addEventListener('click', onClick);
        }
    }

    function listenLogin() {
        if (listenInterval != null) return;
        
        listenInterval = setInterval(function() {
            var oReq = new XMLHttpRequest();
            oReq.onload = reloadPage;
            oReq.open('HEAD', '/checkLogin', true);
            oReq.send()
        }, 1000);
    }

    function reloadPage(e) {
        if (e.target.status === 200) {
            window.location.reload(true);
        }
    }

    function onClick(e) {
        e.preventDefault();
        listenLogin();
        try {
            if (window.navigator.userAgent.match(/Android/)) {
                window.location = this.dataset['androidIntent'];
            } else {
                showQR(this.href);
            }
        } catch(e) {
            console.log(e);
            showQR(this.href);
        }
    }

    function showQR(url) {
        var code = document.getElementById("code");
        code.innerHTML = null;
        var image = new Image();
        image.src = url + '?' + new Date().getTime();
        code.appendChild(image);
    }
})();
