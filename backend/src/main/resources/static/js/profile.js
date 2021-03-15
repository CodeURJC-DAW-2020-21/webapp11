
$(document).ready(() => {

    confirmChanges();

});

const displayProfileChangeError = (errorMessage, error = true) => {

    if(error) {
        $("#flash-message:is([class*='text-success'])").removeClass("text-success");
        $("#flash-message:not([class*='text-danger'])").addClass("text-danger");
    } else {
        $("#flash-message:is([class*='text-danger'])").removeClass("text-danger");
        $("#flash-message:not([class*='text-success'])").addClass("text-success");
    }

    $("#flash-message").text(errorMessage);

}

const submitProfileChanges = () => {

    const id = $("#idUser").val();

    let failMessage = "The first name must be alphabetic";
    const firstName = $("#first-name").val();
    let isValid = /^[a-zA-Z -]+$/.test(firstName);
    if(!isValid) { displayProfileChangeError(failMessage); return; }

    failMessage = "The surname must be alphabetic";
    const surname = $("#surname").val();
    isValid = /^[a-zA-Z -]+$/.test(surname);
    if(!isValid) { displayProfileChangeError(failMessage); return; }

    failMessage = "Please introduce a valid email address";
    const email = $("#email").val();
    isValid = /^\w+@[a-zA-Z_]+?(\.[a-zA-Z]{2,3})?$/.test(email);
    if(!isValid) { displayProfileChangeError(failMessage); return; }

    failMessage = "Invalid address (use this format 'Main St. 123, New York')";
    const address = $("#address").val();
    isValid = /^[\w\s.-]+\d+,\s*[\w\s.-]+$/.test(address);
    if(!isValid) { displayProfileChangeError(failMessage); return; }


    const password = $("#password").val();
    if(password !== "") {
        failMessage = "Password must contain at least one digit, one uppercase and must be 8 characters long minimum";
        isValid = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(password);
        if(!isValid) { displayProfileChangeError(failMessage); return; }

        failMessage = "The two password do not match";
        const confirmPassword = $("#confirm-password").val();
        isValid = password === confirmPassword;
        if(!isValid) { displayProfileChangeError(failMessage); return; }
    }

    const form = $("<form></form>");
    form.attr("method", "post");
    form.attr("action", "/user/"+ id +"/update");
    form.attr("id", "update-profile-form");
    form.attr("enctype", "multipart/form-data");

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