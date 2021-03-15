$(document).ready(() => {

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

    const flashMessage = $('#flash-message');
    if(flashMessage.length) {

        const disableButton = (identifier) => {
            $(identifier).addClass("disabled");
            $(identifier).removeClass("btn-outline-success");
            $(identifier).removeClass("btn-outline-danger");
            $(identifier).removeClass("btn-outline-light");
            $(identifier).addClass("btn-outline-secondary");
        }

        disableButton("#renew-button");
        disableButton("#cancel-button");
        disableButton("#start-server");
        disableButton("#restart-server");
        disableButton("#stop-server");

        $("#server-ip-address").val("N/A");
        $("#server-port").val("N/A");
        $("#server-user").val("N/A");
        $("#server-pass").val("N/A");

        stopAllBarsFluctuation();
    }

});

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

