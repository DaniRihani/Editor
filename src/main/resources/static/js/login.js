// Function to handle login
async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const loginData = {
        username: username,
        password: password
    };

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(loginData),
        });

        const data = await response.json();

        if (response.ok) {
            // Successful login
            alert('Login successful!');
            window.location.href = '/dashboard'; // Redirect to another page
        } else {
            // Handle failed login
            alert('Login failed: ' + data.message);
        }
    } catch (error) {
        console.error('Error during login:', error);
    }
}

// Attach the login function to the login button
document.getElementById('loginBtn').addEventListener('click', login);
