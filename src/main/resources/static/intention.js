(function() {
    'use strict';

    if (document.readyState != 'loading'){
        ready();
    } else {
        document.addEventListener('DOMContentLoaded', ready);
    }

    function ready() {
        console.log('ready');
        var links = document.querySelectorAll('.intent');
        for (var i = 0; i < links.length; ++i) {
            links[i].addEventListener('click', onClick);
        }
    }

    function onClick(e) {
        e.preventDefault();
        try {
            if (window.navigator.userAgent.match(/Android/)) {
                window.location = this.dataset['androidIntent'];
            } else {
                window.location = this.href;
            }
        } catch(e) {
            console.log(e);
            window.location = this.href;
        }
    }
})();
