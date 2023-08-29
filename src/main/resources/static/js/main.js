document.addEventListener("DOMContentLoaded", function() {
    // Fetch the user token from local storage
    var token = localStorage.getItem("userToken");

    // Get references to the login and logout elements
    var loginButton = document.getElementById("loginButton");
    var logoutForm = document.getElementById("logoutForm");

    // If the user is logged in (token exists), hide the login button and show the logout button
    if (token) {
        loginButton.style.display = "none";
        logoutForm.style.display = "block";
    }
    // If the user is not logged in (no token), show the login button and hide the logout button
    else {
        loginButton.style.display = "block";
        logoutForm.style.display = "none";
    }
});