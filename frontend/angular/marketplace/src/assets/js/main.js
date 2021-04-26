$(document).ready(() => {

  $("#year").text(new Date().getFullYear());

  registerNavigationEvents();
  registerCarouselSettings();
  registerServiceOverview();
  registerPanel();
  registerAboutBox();

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
 * Register carousel default properties (if carousel exists)
 */
const registerCarouselSettings = () => {
  const carousel = $(".carousel");
  if(carousel.length) carousel.carousel({ interval: 6000 });
}

const registerServiceOverview = () => {

  if($("#start-server").length) {
    let progressHandler = null;

    const stopAllBarsFluctuation = () => {
      clearInterval(progressHandler);
      resetAllBarsProgress();
      changeButtonsStatus(false);
    }

    const startAllBarsFluctuation = () => {
      progressHandler = simulateServerStatusFluctuation();
      changeButtonsStatus(true);
    }

    const restartAllBarsFluctuation = () => {
      stopAllBarsFluctuation();
      $(`#start-server`).addClass("d-none");
      changeRestartingServerSpinner(true);
      setTimeout(startAllBarsFluctuation, 5000);
      setTimeout(() => changeRestartingServerSpinner(false), 5000);
    }

    $('#start-server').click(startAllBarsFluctuation);
    $('#stop-server').click(stopAllBarsFluctuation);
    $('#restart-server').click(restartAllBarsFluctuation);

    startAllBarsFluctuation();

  }

}


const changeRestartingServerSpinner = (shouldShow) => {
  if(shouldShow) {
    $(`#restart-server-info`).removeClass("d-none");
  } else {
    $(`#restart-server-info`).addClass("d-none");
  }
}

const changeButtonsStatus = (isRunning) => {
  if(isRunning) {
    $(`#stop-restart-server`).removeClass("d-none");
    $(`#start-server`).addClass("d-none");
  } else {
    $(`#start-server`).removeClass("d-none");
    $(`#stop-restart-server`).addClass("d-none");
  }
}

const changeBarProgress = (statisticName, percentage) => {
  $(`#statistic-${statisticName}`).css("width", `${percentage}%`);
  $(`#statistic-${statisticName}-percentage`).text(`${percentage}`);
}

const changeBarColor = (statisticName, percentage) => {
  const resetColor = () => {
    $(`#statistic-${statisticName}`).removeClass("bg-danger");
    $(`#statistic-${statisticName}`).removeClass("bg-warning");
    $(`#statistic-${statisticName}`).removeClass("bg-success");
  }
  resetColor();

  if(percentage > 0 && percentage < 50) {
    $(`#statistic-${statisticName}`).addClass("bg-success");
  } else if(percentage >= 50 && percentage < 75) {
    $(`#statistic-${statisticName}`).addClass("bg-warning");
  } else {
    $(`#statistic-${statisticName}`).addClass("bg-danger");
  }
}

const simulateServerStatusFluctuation = () => {

  function randomInRange(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
  }

  const fluctuateProgress = () => {
    const currentStatus = {};
    currentStatus["cpu"] = 75;
    currentStatus["ram"] = 50;

    const updateStatus = () => {
      const randomCpu = randomInRange(-2, 5);
      let resultCpu = currentStatus["cpu"] + randomCpu;
      if(resultCpu < 0) resultCpu = 0;
      if(resultCpu > 100) resultCpu = 100;
      changeBarColor("cpu", resultCpu);
      changeBarProgress("cpu", resultCpu);

      const randomRam = randomInRange(-2, 5);
      let resultRam = currentStatus["ram"] + randomRam;
      if(resultRam < 0) resultRam = 0;
      if(resultRam > 100) resultRam = 100;
      changeBarColor("ram", resultRam);
      changeBarProgress("ram", resultRam);
    }

    return setInterval(() => updateStatus(), 1000 );
  }

  return fluctuateProgress();
}

const resetAllBarsProgress = () => {
  changeBarColor("cpu", 0);
  changeBarProgress("cpu", 0);
  changeBarColor("ram", 0);
  changeBarProgress("ram", 0);
}

const registerPanel = () => {
  if($("#weekly-sales-chart").length) {

    const registerContentSwitch = () => {

      $(".content-button").each(function () {
        const currentButton = $(this);

        currentButton.click(() => {
          $(".content").addClass("d-none");
          $(".content-button").removeClass("active");
          currentButton.addClass("active");
          const contentId = currentButton.data('daw-content');
          $("#" + contentId).removeClass("d-none");
        });
      });

    }

    registerContentSwitch();
  }
}

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
