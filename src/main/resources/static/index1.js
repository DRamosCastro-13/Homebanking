let hamburger = document.querySelector(".hamburger");
let navMenu = document.querySelector(".nav-menu");


hamburger.addEventListener("click", () => {
    hamburger.classList.toggle("active");
    navMenu.classList.toggle("active");
})

document.querySelectorAll(".nav-link").forEach(n => n.addEventListener("click", () => {
    hamburger.classList.remove("active");
    navMenu.classList.remove("active");
}))

function showAlert(element) {
    element.addEventListener('click', function () {
        Swal.fire({
            title: "This page is currently under development",
            showClass: {
                popup: `
                animate__animated
                animate__fadeInUp
                animate__faster
                `
            },
            hideClass: {
                popup: `
                animate__animated
                animate__fadeOutDown
                animate__faster
                `
            }
        });
    });
}

// Example usage: Assuming 'alert' and 'alert1' are the classes of the elements you want to trigger the alert
showAlert(document.querySelector('.alert'));
showAlert(document.querySelector('.alert1'));