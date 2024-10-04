(() => {
  'use strict'

  document.body.addEventListener(
    'click',
    function (event) {
      if (event.target.classList.contains('sidebar-menu-item')) {
        document.querySelectorAll('.sidebar-menu-item').forEach(item => {
          item.classList.remove('active');
        });
        event.target.classList.add('active');
      }
    }
  );

  document.body.addEventListener(
    'htmx:afterOnLoad',
    function (event) {
      // console.log('htmx:afterOnLoad event triggered');
      if (event.detail.target.id === `main-div`) {
        displayAdmin();
        // Format the json result
        const element = document.querySelector(`#response-main`);
        // This is added here because the line-numbers class was getting deleted on the 2nd selection
        element.classList.add("line-numbers");
        Prism.highlightElement(element);
      }

      if (event.detail.target.id === `sys-info-div`) {
        displaySysInfo();
      }
    }
  );
})()

function updateToolContent(divId) {
  document.body.addEventListener(
    'htmx:afterOnLoad',
    function (event) {
      if (event.detail.target.id === `result-${divId}`) {
        // Format the tool result
        let responseData = event.detail.target.innerHTML;
        const element = document.querySelector(`#result-${divId}`);
        element.textContent = responseData;
        Prism.highlightElement(element);

        // Make the tool result visible
        document.querySelector(`#display-${divId}`).classList.remove('hidden');
      }
    }
  );
}

function closeToolContent(divId) {
  document.querySelector(`#display-${divId}`).classList.add('hidden');
}

function selectApplicationTab(name) {
  document.querySelectorAll('.nav-link')
    .forEach(item => {
      item.classList.remove('active');
    });

  document.querySelector(`#${name}-tab`).classList.add('active');

  // Hide all validation-data divs
  document.querySelectorAll('.validation-data')
    .forEach(item => {
        item.classList.add('hidden');
      }
    );

  // Show the selected validation-data divs
  document.querySelectorAll(`.${name}-data`)
    .forEach(item => {
        item.classList.remove('hidden');
      }
    );
}

function updateServerBaseUrl() {
  document.getElementById("serverBaseUrl").innerText = window.location.origin;
}
