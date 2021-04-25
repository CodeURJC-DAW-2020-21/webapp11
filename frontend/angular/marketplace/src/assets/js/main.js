$(document).ready(() => {

  registerNavigationEvents();
  registerHighlightEffect();
  registerCarouselSettings();

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

/*
 * Registers a new animation effect (all highlight classes)
 */
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
  $(".highlight").each(function () {
    registerHighlight($(this));
  });
}

/*
 * Register carousel default properties (if carousel exists)
 */
const registerCarouselSettings = () => {
  const carousel = $(".carousel");
  if(carousel.length) carousel.carousel({ interval: 6000 });
}
