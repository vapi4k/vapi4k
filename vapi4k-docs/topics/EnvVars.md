<show-structure depth="2"/>

# Vapi4k Env Vars

## vapi4k-core

<table>
    <tr>
        <td width="200">Name</td>
        <td width="400">Description</td>
        <td width="200">Default Value</td>
    </tr>
    <tr>
        <td>VAPI4K_BASE_URL</td>
        <td>Base URL of Vapi4k Server</td>
        <td><shortcut>http://localhost:8080</shortcut></td>
    </tr>
    <tr>
        <td>VAPI_PUBLIC_KEY</td>
        <td>Vapi public key. Used by web applications.</td>
        <td></td>
    </tr>
    <tr>
        <td>VAPI_PRIVATE_KEY</td>
        <td>Vapi private key. Used by outbound call applications.</td>
        <td></td>
    </tr>
    <tr>
        <td>VAPI_PHONE_NUMBER_ID</td>
        <td>Vapi phone number ID. Used by outbound call applications.</td>
        <td></td>
    </tr>
    <tr>
        <td>VAPI_BASE_URL</td>
        <td>Base URL of Vapi API Server. Used by outbound call applications.</td>
        <td><shortcut>https://api.vapi.ai</shortcut></td>
    </tr>
    <tr>
        <td>IS_PRODUCTION</td>
        <td>Development endpoints and verbose error logging disabled if true.</td>
        <td><shortcut>false</shortcut></td>
    </tr>
    <tr>
        <td>ADMIN_PASSWORD</td>
        <td>Admin page password</td>
        <td><shortcut>admin</shortcut></td>
    </tr>
    <tr>
        <td>DEFAULT_SERVER_PATH</td>
        <td>Default value for application <shortcut>serverPath</shortcut> value</td>
        <td><shortcut>/vapi4k</shortcut></td>
    </tr>
</table>

## vapi4k-dbms

<table>
    <tr>
        <td width="200">Name</td>
        <td width="400">Description</td>
        <td width="200">Default Value</td>
    </tr>
    <tr>
        <td>DBMS_DRIVER_CLASSNAME</td>
        <td>JDBC class name</td>
        <td><shortcut>com.impossibl.postgres.jdbc.PGDriver</shortcut></td>
    </tr>
    <tr>
        <td>DBMS_URL</td>
        <td>JDBC URL</td>
        <td><shortcut>jdbc:pgsql://localhost:5432/postgres</shortcut></td>
    </tr>
    <tr>
        <td>DBMS_USERNAME</td>
        <td>Database username</td>
        <td><shortcut>docker</shortcut></td>
    </tr>
    <tr>
        <td>DBMS_PASSWORD</td>
        <td>DBMS password</td>
        <td><shortcut>docker</shortcut></td>
    </tr>
    <tr>
        <td>DBMS_MAX_POOL_SIZE</td>
        <td>Max DBMS connection pool size</td>
        <td><shortcut>10</shortcut></td>
    </tr>
    <tr>
        <td>DBMS_MAX_LIFETIME_MINS</td>
        <td>Max DBMS connection lifetime (in minutes)</td>
        <td><shortcut>30</shortcut></td>
    </tr>
</table>
