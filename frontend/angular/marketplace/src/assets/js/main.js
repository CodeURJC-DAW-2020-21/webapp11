$(document).ready(() => {
  registerNavigationEvents();
});

/*
 * Returns the current device size (xl, lg, md, sm, xs)
 */
const deviceSize = () => {
  return $('#screen-size-detector').find('div:visible').first().attr('id');
}

/*
 * Registers the event associated to the navigation menu (if the menu exists)
 */
const registerNavigationEvents = () => {
  // If the menu product elements exist (register the menu events)
  if ($("#menu-products-mobile").length && $("#menu-products-desktop").length) {
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
}
