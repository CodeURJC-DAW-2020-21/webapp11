$(document).ready(() => {

    registerContentSwitch();

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
        });
    });
    
}

const weeklySalesElement = document.getElementById('weekly-sales-chart').getContext('2d');

const weeklySalesChart = new Chart(weeklySalesElement, {
    type: 'line',
    data: {
        labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
        datasets: [{
            label: 'Sales in a day',
            backgroundColor: 'rgb(21,76,121)',
            borderColor: 'rgb(6,57,112)',
            data: [0, 10, 5, 2, 20, 30, 45]
        }]
    },
    options: {
        plugins: {
            datalabels: {
                color: '#FFF'
            }
        }
    }
});

const weeklyServicesElement = document.getElementById('weekly-services-chart').getContext('2d');

const weeklyServicesChart = new Chart(weeklyServicesElement, {
    type: 'pie',
    data: {
        labels: ['Shared', 'VPS', 'Dedicated'],
        datasets: [{
            data: [10, 5, 5],
            backgroundColor: ["	#ADD8E6", "#1E90FF", "	#0000FF"],
            hoverBackgroundColor: ['#5cb85c', '#5cb85c', '#5cb85c']
        }]
    }
});