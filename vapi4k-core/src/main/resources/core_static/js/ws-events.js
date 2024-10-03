(() => {
  'use strict'

  document.addEventListener('htmx:wsOpen', function (event) {
    console.log('WebSocket connection opened:', event.detail);
  });

  document.addEventListener('htmx:wsClose', function (event) {
    console.log('WebSocket connection closed:', event.detail);
  });

  document.addEventListener('htmx:wsError', function (event) {
    console.error('WebSocket error:', event.detail.error);
  });
})()
