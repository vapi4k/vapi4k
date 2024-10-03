const defaultHeaders = {
  Accept: "application/json"
}

async function fetchJson(fetchMethod, url, userHeaders, jsonBody) {
  try {
    let fetchArg = {
      method: "POST",
      headers: {...defaultHeaders, ...userHeaders},
    }

    // Add json body if one is present and it is a post request
    if (jsonBody && fetchMethod && fetchMethod.toUpperCase() === "POST") {
      fetchArg.body = JSON.stringify(jsonBody)
    }

    const response = await fetch(url, fetchArg);

    if (response.ok) {
      return await response.json()
    } else {
      if (response.status === 404) {
        const urlObj = new URL(url);
        const fullPath = urlObj.pathname;
        const path = fullPath.split('/').pop();
        console.error(
          'Error:',
          Error(
            `Invalid VAPI4K_BASE_URL env value or invalid serverPath "/${path}" used in assistant definition: ${stripQueryParams(
              url)}`)
        );
      } else {
        console.error('Error:', Error(`HTTP error status ${response.status} fetching: ${stripQueryParams(url)}`));
      }
    }
  } catch (error) {
    console.error('Error:', error);
  }
}

function stripQueryParams(url) {
  const urlObj = new URL(url);
  urlObj.search = '';
  return urlObj.toString();
}

function addVapiButton(vapi4kUrl, serverSecret, vapiPublicApiKey, method, postArgs) {
  fetchJson(method, vapi4kUrl, {"x-vapi-secret": serverSecret}, postArgs,)
    .then((response) => {
      (function (document, t) {
        const elem = document.createElement(t);
        elem.src = "https://cdn.jsdelivr.net/gh/VapiAI/html-script-tag@latest/dist/assets/index.js";
        elem.defer = true;
        elem.async = true;
        const scriptTag = document.getElementsByTagName(t)[0];
        scriptTag.parentNode.insertBefore(elem, scriptTag);
        elem.onload = function () {
          const config = buildVapiConfig(response, vapiPublicApiKey);
          const vapi = window.vapiSDK.run(config);
          // logVapiEvents(vapi);
        };
      })(document, "script");
    });
}

function buildVapiConfig(response, publicApiKey) {
  let vapiConfig = {
    apiKey: publicApiKey
  }

  if (response.hasOwnProperty('assistant')) {
    vapiConfig.assistant = response.assistant
  } else if (response.hasOwnProperty('squad')) {
    vapiConfig.squad = response.squad
  } else if (response.hasOwnProperty('assistantId')) {
    vapiConfig.assistantId = response.assistantId
  } else if (response.hasOwnProperty('squadId')) {
    vapiConfig.squadId = response.squadId
  } else {
    console.error('Error:', Error(`Assistant, Squad, AssistantId, or SquadId not found in response`));
  }

  if (response.assistantOverrides) {
    vapiConfig.assistantOverrides = response.assistantOverrides
  }

  if (response.buttonConfig) {
    vapiConfig.config = response.buttonConfig
  }

  return vapiConfig
}

function logVapiEvents(vapi) {
  if (vapi) {
    vapi.on("call-start", () => {
      console.log("Call has started.");
    });

    vapi.on("call-end", () => {
      console.log("Call has ended.");
    });

    vapi.on("speech-start", () => {
      console.log("Assistant speech has started.");
    });

    vapi.on("speech-end", () => {
      console.log("Assistant speech has ended.");
      // vapi.say("Our time's up, goodbye!", true)
    });

    vapi.on("message", (message) => {
      console.log(message);
    });

    // vapi.on("volume-level", (volume) => {
    //   console.log(`Assistant volume level: ${volume}`);
    // });

    vapi.on("error", (e) => {
      console.error(e);
    });
  }
}
