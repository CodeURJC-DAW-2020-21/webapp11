let currentPage = 1;

$(document).ready(() => {

    loadOrders();
    registerLoadMoreAnimation();

});

const loadOrders = () => {
    $.get(`/services/${currentPage}/10`, (data) => {
        $(`${data}`).appendTo("#services-list");
        currentPage++;
    });
}

const registerLoadMoreAnimation = () => {
    $(".load-more-button").each(function(){
        const currentButton = $(this);
        currentButton.click(() => {
            currentButton.addClass("d-none");
            const contentId = currentButton.data('daw-loading-element');
            $("#" + contentId).removeClass("d-none");
            loadOrders();
            const addButtonBack = () => {
                currentButton.removeClass("d-none");
                $("#" + contentId).addClass("d-none");
            }
            setTimeout(() => addButtonBack(), 500);
        });
    });
}