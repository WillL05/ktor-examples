# Forms-Based Authentication Demo

An example of how authentication can be added to a Ktor web application. The
application provides a private page that can be visited only by registered
users who have signed in with a username and password.

> [!NOTE]
> This is not intended to be functionally complete or especially secure
> (although it does protect stored passwords).

## Discussion of Code

`UserDatabase.kt` provides a store of usernames and password hashes, to
support user logins. For simplicity, and to reduce the number of application
dependencies, this data store is based on a simple CSV file rather than an
RDBMS. Password hashing is done securely, using the [Password4j][p4j] library.

`Authentication.kt` installs Ktor's [Authentication plugin][auth] and
sets up

* [Forms-based authentication][form] to support user logins
* [Session-based authentication][sess] to protect the private page

`Sessions.kt` installs the Sessions plugin and defines a data class to
represent the details of a session (username and visit count for the private
page). Session details are stored client side as a cookie that expires after
60 seconds.

Note that this part of the application is crude and could be improved
significantly - e.g., by signing the cookie, or by storing session data on
the server side and providing the client with a securely-generated session ID
as the cookie. See the Ktor documentation for discussion of how to make these
improvements.

`Routing.kt` configures routing for

* The application's home page
* A user registration form
* A request handler for registration form submission
* A user login form
* A request handler for login form submission
* A request handler for logging out of the application
* A private page, accessible once a user has logged in and created a session

See `templates/resources` for the [Pebble templates][peb] that are rendered
by this request handling code.

## Running The Demo

Build and run the server with

    ./amper run

Then visit `http://0.0.0.0:8080/` to access the demo application.

Attempts to visit the private page should initially be denied. It should
also not be possible to sign in when you run the application for the very
first time, as there are no stored credentials.

You should be able to sign up with a username and possword that satisfy the
requirements listed on the user registration page. After that point, you
should be able to use your chosen username and password to sign in.

Take a look at `auth.csv` to see how usernames and password hashes are stored.

After logging in, try visiting the private page multiple times. You will see
a counter that increments by 1 on each visit.

After revisiting the page a few times, wait at least 60 seconds before
attempting to revisit again. The session cookie should have expired by this
point, and you should be prompted to login again.


[p4j]: https://password4j.com/
[auth]: https://api.ktor.io/ktor-server-auth/io.ktor.server.auth/-authentication/index.html
[form]: https://ktor.io/docs/server-form-based-auth.html
[sess]: https://ktor.io/docs/server-session-auth.html
[peb]: https://pebbletemplates.io/
