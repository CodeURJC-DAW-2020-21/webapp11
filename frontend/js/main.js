$(document).ready(() => {

    registerNavigation();
    registerHighlightEffect();
    registerLoginToggler();
    registerLoadMoreAnimation();

});

const deviceSize = () => {
    return $('#screen-size-detector').find('div:visible').first().attr('id');
}

const registerNavigation = () => {

    const deviceNavigationStyleSwitcher = () => {
        const size = deviceSize();
        if (size === "lg" || size === "xl" || size === "md") {
            $("#menu-products-mobile:not([class*='d-none'])").addClass("d-none");
            $("#menu-products-desktop:is([class*='d-none'])").removeClass("d-none");

        } else {
            $("#menu-products-desktop:not([class*='d-none'])").addClass("d-none");
            $("#menu-products-mobile:is([class*='d-none'])").removeClass("d-none");
        }
    }
    $(window).on('load', deviceNavigationStyleSwitcher);
    $(window).on('resize', deviceNavigationStyleSwitcher);

}

const registerHighlightEffect = () => {

    const registerHighlight = (element) => {

        const getColorForEvent = (eventName) => {
            const color = "rgba(255, 255, 255, {opacity})";
            const opacity = eventName === "mouseenter" ? "0.1" : "0.025";
            return color.replace("{opacity}", opacity);
        };

        const performColorSwitch = (eventName) => {
            element.css("background-color", getColorForEvent(eventName));
        };

        element
            .on("mouseenter", (event) => performColorSwitch(event.type, element))
            .on("mouseleave", (event) => performColorSwitch(event.type, element));
    };

    $(".highlight").each(function () { registerHighlight($(this)); });

}

const registerLoginToggler = () => {

    const updateLoginStatus = () => {
        if(localStorage.getItem("logged-in") === "true") {
            $("#logged-in").removeClass("d-none");
            $("#not-logged-in").addClass("d-none");
            $('#login-toggler').prop('checked', true);
        } else {
            $("#not-logged-in").removeClass("d-none");
            $("#logged-in").addClass("d-none");
            $('#login-toggler').prop('checked', false);
        }
    }

    updateLoginStatus();

    $('#login-toggler').click(function() {
        if ($(this).is(':checked')) {
            localStorage.setItem("logged-in", "true");
        } else {
            localStorage.setItem("logged-in", "false");
        }
        updateLoginStatus();
    });

}

const registerLoadMoreAnimation = () => {

    $(".load-more-button").each(function(){
        const currentButton = $(this);
        console.log("hi");

        currentButton.click(() => {
            currentButton.addClass("d-none");
            const contentId = currentButton.data('daw-loading-element');
            $("#" + contentId).removeClass("d-none");

            const addButtonBack = () => {
                currentButton.removeClass("d-none");
                $("#" + contentId).addClass("d-none");
            }

            setTimeout(() => addButtonBack(), 1000);
            
        });
    });
    

}