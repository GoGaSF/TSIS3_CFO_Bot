const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const authMessage = document.getElementById("authMessage");

function showMessage(text) {
    authMessage.textContent = text;
    authMessage.classList.remove("hidden");
}

function hideMessage() {
    authMessage.textContent = "";
    authMessage.classList.add("hidden");
}

function switchToLogin() {
    loginTab.classList.add("active");
    registerTab.classList.remove("active");
    loginForm.classList.remove("hidden");
    registerForm.classList.add("hidden");
    hideMessage();
}

function switchToRegister() {
    registerTab.classList.add("active");
    loginTab.classList.remove("active");
    registerForm.classList.remove("hidden");
    loginForm.classList.add("hidden");
    hideMessage();
}

loginTab.addEventListener("click", switchToLogin);
registerTab.addEventListener("click", switchToRegister);

async function checkSession() {
    try {
        const response = await fetch("/api/auth/me");
        const data = await response.json();

        if (data.success) {
            window.location.href = "/";
        }
    } catch (error) {
        console.error(error);
    }
}

loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    hideMessage();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (!response.ok || !data.success) {
            showMessage(data.message || "Login failed");
            return;
        }

        window.location.href = "/";
    } catch (error) {
        showMessage("Unable to login right now.");
        console.error(error);
    }
});

registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    hideMessage();

    const name = document.getElementById("registerName").value;
    const email = document.getElementById("registerEmail").value;
    const password = document.getElementById("registerPassword").value;

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ name, email, password })
        });

        const data = await response.json();

        if (!response.ok || !data.success) {
            showMessage(data.message || "Registration failed");
            return;
        }

        window.location.href = "/";
    } catch (error) {
        showMessage("Unable to register right now.");
        console.error(error);
    }
});

checkSession();