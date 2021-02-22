$(document).ready(() => {

    registerLoginAction();

});

const displayLoginError = (errorMessage, error = true) => {

    if(error) {
        $("#flash-message:is([class*='text-success'])").removeClass("text-success");
        $("#flash-message:not([class*='text-danger'])").addClass("text-danger");
    } else {
        $("#flash-message:is([class*='text-danger'])").removeClass("text-danger");
        $("#flash-message:not([class*='text-success'])").addClass("text-success");
    }

    $("#flash-message").text(errorMessage);

}

const registerLoginAction = () => {

    $("#login-button").click(() => {

        const failMessage = "The provided credentials are not correct.";
        const successMessage = "Successfully logged in, redirecting...";
            
        const username = $("#username").val();
        if(username === "") { displayLoginError(failMessage); return; }

        const password = $("#password").val();
        if(password === "") { displayLoginError(failMessage); return; }

        const requestData = { username: username, password: password };
        /*
            In the future just use POST AJAX request login and jwt storage
        */
        displayLoginError(successMessage, false);
        
    });

}