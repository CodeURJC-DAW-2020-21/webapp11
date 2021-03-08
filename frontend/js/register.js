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

const registerRegistrationAction = () => {

    $("#register-button").click(() => {

        const successMessage = "Registration successful, redirecting...";
        
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
        isValid = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/.test(email);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "Invalid address (use this format 'Main St. 123, New York')";
        const address = $("#address").val();
        isValid = /^[\w\s.-]+\d+,\s*[\w\s.-]+$/.test(address);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "Password must contain at least one digit, one uppercase and must be 8 characters long minimum";
        const password = $("#password").val();
        isValid = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/.test(password);
        if(!isValid) { displayLoginError(failMessage); return; }

        failMessage = "The two password do not match";
        const confirmPassword = $("#confirm-password").val();
        isValid = password === confirmPassword;
        if(!isValid) { displayLoginError(failMessage); return; }

        const requestData = {
            first_name: firstName,
            surname: surname,
            email: email,
            address: address,
            password: password
        };

        alert(JSON.stringify(requestData));
        
        /*
        In the future just use POST AJAX request registration
        */
        displayLoginError(successMessage, false);
        
    });

}