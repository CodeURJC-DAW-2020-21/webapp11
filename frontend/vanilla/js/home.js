$(document).ready(() => {

    registerCarouselSettings();

});

const registerCarouselSettings = () => {
    $(".carousel").carousel({ 
        interval: 6000 
    });
}