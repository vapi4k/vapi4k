(() => {
  'use strict'

  document.addEventListener('htmx:beforeRequest',
                            function (event) {
                              const target = document.querySelector('#main-div');
                              target.classList.toggle('fade-in');
                              target.classList.toggle('fade-out');
                            });

  document.addEventListener('htmx:afterSwap',
                            function (event) {
                              const target = document.querySelector('#main-div');
                              target.classList.toggle('fade-out');
                              target.classList.toggle('fade-in');
                            });
})()
