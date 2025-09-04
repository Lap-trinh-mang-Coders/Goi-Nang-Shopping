$(document).ready(function () {
    const LOGIN_URL = 'http://localhost:2025/api/auth/login';

    $('#loginForm').on('submit', function (event) {
        event.preventDefault();

        const email = $('#email').val();
        const password = $('#password').val();

        $.ajax({
            url: LOGIN_URL,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email, password: password }),
            // In js/app/login.js
            success: function (response) {
                if (response.token) {
                    localStorage.setItem('accessToken', response.token);
                    // Simple redirect, no full URL needed
                    window.location.href = 'item_management.html';
                }
            },
            error: function () {
                $('#errorMessage').text('Invalid email or password.');
            }
        });
    });
});