<show-structure depth="1"/>

# Setup

## Create a Vapi Account

Create a Vapi account by going [here](https://dashboard.vapi.ai).

## Get a Vapi Phone Number

<procedure>
    <step>
        <p>Go to the <a href="https://dashboard.vapi.ai">Vapi dashboard</a>.</p>
    </step>
    <step>
        <p>Click on <shortcut>Platform</shortcut>, <shortcut>Phone Numbers</shortcut>, and then
            <shortcut>Buy Number</shortcut> to get a phone number.</p>
    </step>
</procedure>

## Install IntelliJ

To develop Vapi4k applications, you need to install IntelliJ on your machine.
You can download the Community Edition from the [JetBrains Toolbox App](https://www.jetbrains.com/toolbox-app/).

## Configure IntelliJ

The [EnvFile](https://plugins.jetbrains.com/plugin/7861-envfile) plugin allows you to load environment variables from a
file when running your applications. It simplifies
local development with `ngrok`.

<procedure title="Install the EnvFile plugin">
    <step>
        <p>Start IntelliJ.</p>
    </step>
    <step>
        <p>Select the <shortcut>IntelliJ IDEA</shortcut> <shortcut>Settings...</shortcut> menu options.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Plugins</shortcut> option.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Marketplace</shortcut> option and type <b>EnvFile</b> in the search field.</p>
    </step>
    <step>
        <p>Enable the <shortcut>EnvFile</shortcut> option.</p>
    </step>
    <step>
        <p>Restart IntelliJ.</p>
    </step>
</procedure>

## Create a repository

<procedure title="Create a vapi4k-template repository">
    <step>
        <p>Go to the <a href="https://github.com/vapi4k/vapi4k-template">vapi4k-template</a> GitHub page.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Use this template</shortcut> and <shortcut>Create a new repository</shortcut> options.</p>
    </step>
    <step>
        <p>Give the new repository a name.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Create repository</shortcut> option.</p>
    </step>
</procedure>

## Clone your repository

You can now clone your new vapi4k-template repository to your local machine from within IntelliJ or using
`git clone <repository-url>`
on the command line.

<procedure title="Clone the vapi4k-template repository within IntelliJ">
    <step>
        <p>Go to your newly created vapi4k-template repository page on GitHub.</p>
    </step>
    <step>
        <p>Click on the <shortcut>Code</shortcut> option.</p>
    </step>
    <step>
        <p>Copy either the <shortcut>HTTPS</shortcut> or <shortcut>SSH</shortcut> repository URL.</p>
    </step>
    <step>
        <p>Open IntelliJ.</p>
    </step>
    <step>
        <p>Click on the <shortcut>File</shortcut>, <shortcut>New</shortcut>, <shortcut>Project from Version Control...</shortcut> options.</p>
    </step>
    <step>
        <p>Paste the repository URL and choose the desired <shortcut>Directory</shortcut>.</p>
    </step>
</procedure>

## Install ngrok

The [ngrok](https://ngrok.com/) proxy allows you to expose your local Vapi4k Ktor server to the internet.
It is very useful for developing Vapi4k apps because it greatly decreases your iteration time.
It is also useful for watching the traffic between your app and the Vapi platform.
To install ngrok, go to the [ngrok download page](https://ngrok.com/download) and follow the instructions for
your operating system.

## Run ngrok

To run ngrok, open a terminal window and type:

```bash
ngrok http 8080
```

Copy the ngrok `Web Interface` URL and paste it into your browser. You will see the ngrok dashboard and all messages
exchanged between your app and the Vapi platform.

Copy the ngrok `Forwarding` URL to your clipboard. You will need this URL in the next step.

## Create a secrets file

The Vapi4k server requires some environment variables to be defined to connect to the Vapi platform.

<procedure title="Create a secrets.env file">
    <step>
        <p>Start IntelliJ.</p>
    </step>
    <step>
        <p>Open your vapi4k-template application.</p>
    </step>
    <step>
        <p>Ctrl-click on the <shortcut>/secrets</shortcut> folder.</p>
    </step>
    <step>
        <p>Choose the <shortcut>New</shortcut>, <shortcut>File</shortcut> options.</p>
    </step>
    <step>
        <p>Create a file named <b>secrets.env</b>.</p>
    </step>

The `secrets.env` file should contain values for these environment variables:

* VAPI_PRIVATE_KEY
* VAPI_PUBLIC_KEY
* VAPI_PHONE_NUMBER_ID
* VAPI4K_BASE_URL

The first three values can be found on the Vapi dashboard.
The keys are found by clicking on the organization button on the lower left,
and the phone number ID is found in the `Platform` `Phone Number` section.

The VAPI4K_BASE_URL value is the ngrok `Forwarding` URL.

The `secrets/secrets.env` should look something like this:

```bash
VAPI_PRIVATE_KEY=f3ff6277-8d9b-8873-eec7-743786e2aa42
VAPI_PUBLIC_KEY=l9lf6233-9s9f-9173-egc1-433786e2aa98
VAPI_PHONE_NUMBER_ID=8b151b80-5fff-4df9-ad67-993189409d4c
VAPI4K_BASE_URL=https://fdee-73-71-109-432.ngrok-free.app
```

> **Keeping secrets.env private**
>
> Make sure `secrets.env` is not added to your repository. The `secrets` folder is already in the .gitignore file.
>
>
{style="warning"}

</procedure>

## Adjust Run Configuration

<procedure title="Adjust Run Configuration to use secrets.env">
    <step>
        <p>Start IntelliJ.</p>
    </step>
    <step>
        <p>Open <b>src/main/kotlin/com/myapp/Application.kt</b>.</p>
    </step>
    <step>
        <p>Click on the green arrow to the left of <code>fun main()</code> to run the server.</p>
    </step>
    <step>
        <p>Now run the server with the secrets.env by clicking on the three vertical dots in the Run
          panel and click on <shortcut>Modify Run Configuration...</shortcut>.</p>
    </step>
    <step>
        <p>Scroll down and check the <shortcut>Enable Env File</shortcut> option and assign the <shortcut>Server URL</shortcut>.</p>
    </step>
    <step>
        <p>Scroll down and click on the <shortcut>➕</shortcut> button, then click on <shortcut>.env file</shortcut>
          and then select the <shortcut>secrets.env</shortcut> created above.</p>
    </step>
    <step>
        <p>Restart the server and you should see the log saying the <shortcut>Vapi4kServer is started at</shortcut>
          with the ngrok VAPI4K_BASE_URL value.</p>
    </step>
    <step>
        <p>Click on the VAPI4K_BASE_URL value in the log to open the Vapi4k admin page.
          Use the username/password values of admin/admin.</p>
    </step>
</procedure>

You are now ready to run a Vapi4k application.
