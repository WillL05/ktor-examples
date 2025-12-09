# Single-Page Dice Rolling Simulator

A single-page application that simulates the rolling of a one or more d4, d6,
d8, d10, d12 or d20 dice.

The application uses [htmx][hx] to update the page without requiring a full page
reload. The page is rendered using [Pebble][peb] templates.

Compare this with `dice-roller`.

Build and run the server with

    ./amper run

Then visit `http://0.0.0.0:8080/` to choose your dice.


[hx]: https://htmx.org/
[peb]: https://pebbletemplates.io/
