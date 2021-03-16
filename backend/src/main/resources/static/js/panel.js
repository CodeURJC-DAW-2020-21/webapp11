let currentPage = 1;

$(document).ready(() => {

    registerContentSwitch();
    registerLoadMoreAnimation();
    loadUsers();
    toggleAccount();
    loadWithDataWeeklySalesChart();
    loadWithDataWeeklyServicesChart();
    registerSaveOtd();

    $("#current-date").text(moment().format('ddd DD MMM, YYYY'));

    const id = $("#otd-pid").text();
    if(id !== "") {
        $(`#otd-packages option[value=${id}]`).attr('selected','selected');
    }

});

const dateToString = (identifier) => {
    const date = new Date($(identifier).val());
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    return [day, month, year].join('/');
}

const registerSaveOtd = () => {
    $("#otd-update").click(() => {
        const start = dateToString("#otd-start");
        const stop = dateToString("#otd-stop");
        const discount = $("#otd-discount").val();
        const pack = $('#otd-packages option:selected').val();
        const sentInfo = {
            startDate: start,
            stopDate: stop,
            discountPercentage: discount,
            productId: pack
        };
        $.post( "/sale/otd/update", sentInfo)
            .done(function( data ) {
                if($("#otd-message").length) {
                    $("#otd-message").empty();
                }
                $(`${data}`).appendTo("#otd-message");
                setTimeout(function() {
                    if($("#otd-message").length) {
                        $("#otd-message").empty();
                    }
                }, 3000);
        });
    });
}

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
    $.get(`/users/${currentPage}/10`, (data) => {
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
            let elementsAmount = (lastPage - 1) * 10;
            $.get(`/users/1/${elementsAmount}`, (data) => {
                $(`${data}`).appendTo("#clients");
                currentPage = lastPage;
            });
            document.getElementById("flash-spinner").outerHTML = "";
        }
        setTimeout(() => updateLoaded(), 10);
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

const loadWithDataWeeklySalesChart = () => {
    const sales = [];
    $("#sales-per-day-in-week > span").each(function() {
        const input = $(this);
        sales.push(input.text());
    })
    const options = {
        type: 'line',
        data: {
            labels: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
            datasets: [{
                label: 'Sales in a day',
                backgroundColor: 'rgb(21,76,121)',
                borderColor: 'rgb(6,57,112)',
                data: sales
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

const loadWithDataWeeklyServicesChart = () => {
    const capitalize = (s) => { return s && s[0].toUpperCase() + s.slice(1); };
    const categories = [];
    const sales = [];
    const predefinedColors = [ "#002B4B", "#0067B5", "#32556E" ];
    const colors = [];
    let currentColor = 1;
    const getColor = () => { let v = currentColor%3; if(v > 2) v = 0; currentColor++; return predefinedColors[v]; };
    const hover = [];
    $("#weekly-category-purchases > span").each(function() {
        const input = $(this);
        const content = input.text();
        const category = capitalize(content.split(":")[0]);
        const sale = Number(content.split(":")[1]);
        categories.push(category);
        sales.push(sale);
        colors.push(getColor());
        hover.push("#3285C3");
    })
    const options = {
        type: 'pie',
        data: {
            labels: categories,
            datasets: [{
                data: sales,
                backgroundColor: colors,
                hoverBackgroundColor: hover
            }]
        }
    };
    const element = document.getElementById('weekly-services-chart').getContext('2d');
    new Chart(element, options);
}