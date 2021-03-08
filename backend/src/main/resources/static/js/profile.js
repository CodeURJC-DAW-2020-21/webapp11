
$(document).ready(() => {

    confirmChanges();

});

const submitProfileChanges = () => {

    const id = $("#idUser").val();
    const firstName = $("#firstName").val();
    const surname = $("#surname").val();
    const email = $("#email").val();
    const address = $("#address").val();
    const password = $("#password").val();

    const form = $("<form></form>");
    form.attr("method", "post");
    form.attr("action", "/user/"+ id +"/update");

    const idField = $("<input></input>");
    idField.attr("type", "hidden");
    idField.attr("name", "id");
    idField.attr("value", id);
    form.append(idField);

    const firstNameField = $("<input></input>");
    firstNameField.attr("type", "hidden");
    firstNameField.attr("name", "firstName");
    firstNameField.attr("value", firstName);
    form.append(firstNameField);

    const surnameField = $("<input></input>");
    surnameField.attr("type", "hidden");
    surnameField.attr("name", "surname");
    surnameField.attr("value", surname);
    form.append(surnameField);

    const emailField = $("<input></input>");
    emailField.attr("type", "hidden");
    emailField.attr("name", "email");
    emailField.attr("value", email);
    form.append(emailField);

    const addressField = $("<input></input>");
    addressField.attr("type", "hidden");
    addressField.attr("name", "address");
    addressField.attr("value", address);
    form.append(addressField);

    const passwordField = $("<input></input>");
    passwordField.attr("type", "hidden");
    passwordField.attr("name", "password");
    passwordField.attr("value", password);
    form.append(passwordField);


    $(document.body).append(form);
    form.submit();

}

const confirmChanges = () => {

    $("#confirm-changes-button").click(() => {
        submitProfileChanges();
    });

    $('.input').keypress(function(event) {
        if (event.which === 13) {
            submitProfileChanges();
        }
    });

}