/*!
 * GoBees 2017 - David Miguel Lozano
 * Theme based on:
 * Start Bootstrap - New Age v3.3.7 (http://startbootstrap.com/template-overviews/new-age)
 * Copyright 2013-2016 Start Bootstrap
 * Licensed under MIT (https://github.com/BlackrockDigital/startbootstrap/blob/gh-pages/LICENSE)
 */

(function ($) {
    "use strict"; // Start of use strict

    // jQuery for page scrolling feature - requires jQuery Easing plugin
    $('a.page-scroll').bind('click', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top - 50)
        }, 1250, 'easeInOutExpo');
        event.preventDefault();
    });

    // Highlight the top nav as scrolling occurs
    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 100
    });

    // Closes the Responsive Menu on Menu Item Click
    $('.navbar-collapse ul li a').click(function () {
        $('.navbar-toggle:visible').click();
    });

    // Offset for Main Navigation
    $('#mainNav').affix({
        offset: {
            top: 50
        }
    });

    // Change screen shots
    var screenshot = $('#screenshot');
    var images = [
        "img/screenshots/apiaries.png",
        "img/screenshots/apiary.png",
        "img/screenshots/apiary-info.png",
        "img/screenshots/hive.png",
        "img/screenshots/monitoring.png",
        "img/screenshots/recording.png"
    ];
    var current = 0;

    function nextBackground() {
        screenshot.fadeOut(400, function () {
            screenshot.attr("src", images[current = ++current % images.length]);
        }).fadeIn(400);
        setTimeout(nextBackground, 4000);
    }

    setTimeout(nextBackground, 4000);

})(jQuery); // End of use strict
