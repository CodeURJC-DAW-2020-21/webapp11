$(document).ready(() => {

    registerLoginInteractions();

});

const submitLogin = () => {

    const username = $("#username").val();
    const password = $("#password").val();

    const form = $("<form></form>");
    form.attr("method", "post");
    form.attr("action", "/login");

    const usernameField = $("<input></input>");
    usernameField.attr("type", "hidden");
    usernameField.attr("name", "username");
    usernameField.attr("value", username);
    form.append(usernameField);

    const passwordField = $("<input></input>");
    passwordField.attr("type", "hidden");
    passwordField.attr("name", "password");
    passwordField.attr("value", password);
    form.append(passwordField);

    $(document.body).append(form);
    form.submit();

}

const registerLoginInteractions = () => {

    $("#login-button").click(() => {
        submitLogin();
    });

    $('.form-control').keypress(function(event) {
        if (event.which === 13) {
            submitLogin();
        }
    });

}