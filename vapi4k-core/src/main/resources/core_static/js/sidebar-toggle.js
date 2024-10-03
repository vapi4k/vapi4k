(() => {
  'use strict'

  const hamBurger = document.querySelector(".toggle-btn");

  hamBurger.addEventListener("click", function () {
    document.querySelector("#sidebar").classList.toggle("expand");
  });

  // This will toggle the sidebar open
  document.querySelector("#sidebar").classList.toggle("expand");
})()
