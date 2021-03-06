$(document).ready(() => {

    registerRegistrationAction();

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

const submitRegister = () => {

    const form = $("<form></form>");
    form.attr("name", "registerUser");
    form.attr("method", "post");
    form.attr("action", "/register");

    const elementIds = [ "first-name", "surname", "email", "address", "password" ];
    elementIds.forEach((item) => {

        const field = $("<input></input>");
        field.attr("type", "hidden");
        field.attr("name", item === "first-name" ? "firstName" : item);

        const element = $(`#${item}`);
        field.attr("value", element.val());
        form.append(field);
    });

    $(document.body).append(form);
    form.submit();

}

const registerRegistrationAction = () => {

    $("#register-button").click(() => {

        let failMessage = "The first name must be alphabetic";
        const firstName = $("#first-name").val();
        let isValid = /^[a-zA-Z -]+$/.test(firstName);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "The surname must be alphabetic";
        const surname = $("#surname").val();
        isValid = /^[a-zA-Z -]+$/.test(surname);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "Please introduce a valid email address";
        const email = $("#email").val();
        isValid = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(String(email).toLowerCase());
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "Address can't be empty";
        const address = $("#address").val();
        isValid = address !== "";
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "Password must contain at least one digit, one uppercase and must be 8 characters long minimum";
        const password = $("#password").val();
        isValid = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(password);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "The two password do not match";
        const confirmPassword = $("#confirm-password").val();
        isValid = password === confirmPassword;
        if(!isValid) { displayLoginError(failMessage); return; }

        submitRegister();

    });

    $('.form-control').keypress(function(event) {
        if (event.which === 13) {
            submitRegister();
        }
    });

}