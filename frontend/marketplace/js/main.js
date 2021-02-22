$(document).ready(() => {

    registerNavigation();
    registerHighlightEffect();
    registerLoginToggler();

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

    $('#login-toggler').click(function() {
        if ($(this).is(':checked')) {
            $("#logged-in").removeClass("d-none");
            $("#not-logged-in").addClass("d-none");
        } else {
            $("#not-logged-in").removeClass("d-none");
            $("#logged-in").addClass("d-none");
        }
        console.log("HI");
    });

}

