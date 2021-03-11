let currentPage = 1;

$(document).ready(() => {

    registerContentSwitch();
    registerLoadMoreAnimation();
    loadUsers();
    toggleAccount();
    loadDefaultWeeklySalesChart();
    loadDefaultWeeklyServicesChart();

    $("#current-date").text(moment().format('ddd DD MMM, YYYY'));

});

const registerContentSwitch = () => {
    $(".content-button").each(function(){
        const currentButton = $(this);
        currentButton.click(() => {
            $(".content").addClass("d-none");
            $(".content-button").removeClass("active");
            currentButton.addClass("active");
            const contentId = currentButton.data('daw-content');
            $("#" + contentId).removeClass("d-none");
            $("#flash").replaceWith(`<div id="flash"></div>`);
        });
    });
}

const loadUsers = () => {
    $.get(`/users/${currentPage}/3`, (data) => {
        $(`${data}`).appendTo("#clients");
        currentPage++;
    });
}

const toggleAccount = () => {
    $(document).on('click', '.toggle-account', function() {
        const currentButton = $(this);
        const id = currentButton.data('daw-user-id');
        const type = currentButton.data('daw-user-action');
        const lastPage = currentPage;
        currentPage = 1;
        $.post(`/user/${id}/${type}`, (data) => {
            $("#flash").replaceWith(data);
        });
        const updateLoaded = () => {
            $("#clients").html('');
            let elementsAmount = (lastPage - 1) * 3;
            $.get(`/users/1/${elementsAmount}`, (data) => {
                $(`${data}`).appendTo("#clients");
                currentPage = lastPage;
            });
            document.getElementById("flash-spinner").outerHTML = "";
        }
        setTimeout(() => updateLoaded(), 1000);
    });
}

const registerLoadMoreAnimation = () => {
    $(".load-more-button").each(function(){
        const currentButton = $(this);
        currentButton.click(() => {
            currentButton.addClass("d-none");
            const contentId = currentButton.data('daw-loading-element');
            $("#" + contentId).removeClass("d-none");
            loadUsers();
            const addButtonBack = () => {
                currentButton.removeClass("d-none");
                $("#" + contentId).addClass("d-none");
            }
            setTimeout(() => addButtonBack(), 500);
        });
    });
}

const loadDefaultWeeklySalesChart = () => {
    const options = {
        type: 'line',
        data: {
            labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
            datasets: [{
                label: 'Sales in a day',
                backgroundColor: 'rgb(21,76,121)',
                borderColor: 'rgb(6,57,112)',
                data: [0, 0, 0, 0, 0, 0, 0]
            }]
        },
        options: {
            plugins: {
                datalabels: {
                    color: '#FFF'
                }
            }
        }
    };
    const element = document.getElementById('weekly-sales-chart').getContext('2d');
    new Chart(element, options);
}

const loadDefaultWeeklyServicesChart = () => {
    const options = {
        type: 'pie',
        data: {
            labels: ['No sales to display'],
            datasets: [{
                data: [1],
                backgroundColor: ['#003965'],
                hoverBackgroundColor: ['#003965']
            }]
        }
    };
    const element = document.getElementById('weekly-services-chart').getContext('2d');
    new Chart(element, options);
}

/*
const loadDefaultWeeklyServicesChart = () => {
    const options = {
        type: 'pie',
        data: {
            labels: ['Shared', 'VPS', 'Dedicated'],
            datasets: [{
                data: [0, 0, 0],
                backgroundColor: ["	#ADD8E6", "#1E90FF", "	#0000FF"],
                hoverBackgroundColor: ['#5cb85c', '#5cb85c', '#5cb85c']
            }]
        }
    };
    const element = document.getElementById('weekly-services-chart').getContext('2d');
    new Chart(element, options);
}
*/