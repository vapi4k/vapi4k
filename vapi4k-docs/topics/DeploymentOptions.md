# Deployment Options

## Heroku

[Heroku](https://www.heroku.com) is a cloud platform where you can deploy your Vapi4k applications.

Install the Heroku CLI using these [instructions](https://devcenter.heroku.com/articles/heroku-cli).

<procedure title="Create a Heroku Application">
    <step>
      <p>Open a terminal window and navigate to the root directory of your Vapi4k application.</p>
    </step>
    <step>
      <p>Log in to Heroku with the command <code>heroku login</code>.</p>
    </step>
    <step>
      <p>Create a new Heroku app with the command <code>heroku create 'optional_name'</code>.</p>
    </step>
    <step>
      <p>Push your application to Heroku with the command <code>git push heroku main</code>.</p>
    </step>
    <step>
      <p>Open your application with the command <code>heroku open</code>. The URL in your browser should be
          assigned to VAPI4K_BASE_URL in the next step.</p>
    </step>
    <step>
      <p>Go to [Heroku](https://www.heroku.com) and select the newly created application.</p>
    </step>
    <step>
      <p>Select the <shortcut>Settings</shortcut> tab, click on the <shortcut>Reveal Config Vars</shortcut> button,
        and assign the env vars found in your secrets.env file. Assign VAPI4K_BASE_URL to the URL seen above.</p>
    </step>
    <step>
      <p>Refresh your browser window after the dyno restarts.</p>
    </step>
</procedure>

> **IP Address Mismatch** when trying to log in to Heroku
>
> If you get an **IP address mismatch** error when trying to log in to Heroku from OSX, try
> disabling the iCloud Private Relay option in `System Settings` ➡️`Apple Account` ➡️`iCloud`.
> You might have to restart your browser after making this change.
>
{style="warning"}

> JDK Version
>
> The `java.runtime.version` value in the `system.properties` file must match the `jvmToolchain` value in the
> `build.gradle.kts` file.
>
{style="note"}

## Dockerfile

You can deploy your Vapi4k application using a Docker container.
The `vapi4k-template` project includes a `Dockerfile` that you can use to build a Docker image.

Install and run Docker Desktop by following these [instructions](https://www.docker.com/products/docker-desktop).

Create a Docker account [here](https://app.docker.com/signup).

Assign your Docker username and image name to the `IMAGE_NAME` variable in the `Dockerfile`.

The default jar filename is `vapi4k-template.jar`, but you can change it to whatever name you like.
The name must be changed in both the `Dockerfile` and `build.gradle.kts` file.

Create a Docker image with the following command:
```bash
make build-docker
```

Push the Docker image with the following command:
```bash
make push-docker
```

## Digital Ocean

[Digital Ocean](https://www.digitalocean.com) makes it easy to deploy a Docker container
with their [App Platform](https://www.digitalocean.com/products/app-platform) product.
