<show-structure depth="1"/>

# Run the Template Applications

## Verify the Applications

Before running the applications, verify that the assistant request responses and tool calls for each
of the applications work correctly.

<procedure title="">
    <step>
        <p>Start the Vapi4k server.</p>
    </step>
    <step>
        <p>Click on the VAPI4K_BASE_URL value in the log to open the Vapi4k admin page.</p>
    </step>
    <step>
        <p>Click on <shortcut>/vapi4k</shortcut> under <shortcut>InboundCall Applications</shortcut>.</p>
    </step>
    <step>
        <p>The <shortcut>Assistant Response</shortcut> tab contains the JSON message generated for an inbound call
          using the <shortcut>/incomingCall/vapi4k</shortcut> path.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Service Tools</shortcut> to see the weather tool.</p>
    </step>
    <step>
        <p>Type some values for city and state and click <shortcut>Invoke Tool</shortcut> to see the resulting JSON message.</p>
    </step>
    <step>
        <p>Have a look at the <shortcut>/callCustomer</shortcut> and <shortcut>/talkApp</shortcut> serverPaths as well.</p>
    </step>
</procedure>

## Inbound Call Application

<procedure title="Configure Vapi for an Inbound Call">
    <step>
        <p>Go to the <a href = "https://dashboard.vapi.ai">Vapi dashboard</a>.</p>
    </step>
    <step>
        <p>Click on the green organization button on the lower left.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Settings</shortcut> option and assign the <shortcut>Server URL</shortcut>.</p>
    </step>

The <shortcut>Server URL</shortcut> is a combination of the `VAPI4K_BASE_URL` value and an `/inboundCall/serverPath`
value.

The `/inboundCall` indicates that the URL corresponds to a Vapi4k `inboundCallApplication{}` declaration.

The `serverPath` value is defined in the `inboundCallApplication{}` declaration and
defaults to `/vapi4k`.

The <shortcut>Sever URL</shortcut> should look something like this:

```bash
https://c7dc-2601-644-8722-6250-a138-5443-c3c5-eb1d.ngrok-free.app/inboundCall/vapi4k
```

</procedure>

<procedure title="Run the inbound call application">
    <step>
        <p>Start the Vapi4k server.</p>
    </step>
    <step>
        <p>Call your Vapi phone number and you should see messages in the admin log.</p>
    </step>
    <step>
        <p>To test a tool call, ask the assistant "What is the weather in "Dallas, Texas?"</p>
    </step>
</procedure>

## Outbound Call Application

<procedure title="Run the outbound call application">
    <step>
        <p>Start the Vapi4k server.</p>
    </step>
    <step>
        <p>Open <b>src/main/kotlin/com/myapp/CallCustomer.kt</b> and edit the phone number appropriately.</p>
    </step>
    <step>
        <p>Click on the green arrow to the left of <code>fun main()</code> to create the call.</p>
    </step>
    <step>
        <p>You should now receive a phone call at the specified number.</p>
    </step>
    <step>
        <p>Ask the assistant "What is the weather in Los Angeles, California?"</p>
    </step>
</procedure>

## Web Application

<procedure title="Run the web application">
    <step>
        <p>Start the Vapi4k server.</p>
    </step>
    <step>
        <p>Open the <b>$VAPI4K_BASE_URL/talk</b> URL in your browser.</p>
    </step>
    <step>
        <p>Click on the talk button and ask the assistant "What is the weather in "Miami, Florida?"</p>
    </step>
</procedure>


