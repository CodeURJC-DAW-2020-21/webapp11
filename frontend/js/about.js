$(document).ready(() => {

    registerAboutBox();

});

const registerAboutBox = () => {

    const deviceNavigationStyleSwitcher = () => {
        const size = deviceSize();
        if (size === "lg" || size === "xl") {
            $(".about-box").removeClass("rounded");
            $(".about-box").addClass("rounded-pill");
        } else {
            $(".about-box").removeClass("rounded-pill");
            $(".about-box").addClass("rounded");
        }
    }
    $(window).on('load', deviceNavigationStyleSwitcher);
    $(window).on('resize', deviceNavigationStyleSwitcher);

}
