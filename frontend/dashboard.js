// ---------------------------------------------
// 1. HÄMTA KUND FRÅN LOCAL STORAGE
// ---------------------------------------------
const customer = JSON.parse(localStorage.getItem("customer"));
const token = localStorage.getItem("token");

// Om ingen kund → tillbaka till login
if (!customer) {
    window.location.href = "login.html";
}

// ---------------------------------------------
// 2. VISA KUNDENS NAMN
// ---------------------------------------------
document.getElementById("customerName").textContent = customer.firstname;

// ---------------------------------------------
// 3. LOGGA UT
// ---------------------------------------------
document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("customer");
    localStorage.removeItem("token");
    window.location.href = "login.html";
});
