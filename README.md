# SKELPER

## About

This is a modular bot using the Discord4J framework, dynamically loads plugins in using Groovy. 

## How to run it

Either compile into a jar from source and run it that way or run the jar supplied in the RELEASE zip.

Note that the file config/token must contain only your token and no trailing spaces. If the token is left out it will just silently fail.

Logs can be found in a generated log folder at the jar root. Runs headless with a spring frontend (at localhost:8080) that's currently missing pretty much everything. It does have a button that allows you to shutdown the bot though.

