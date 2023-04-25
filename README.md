# Design By Contract SQL Generator

This is a repository containing implementation for my master thesis `Design and Implementation of a Tool for Generating SQL Routines Based on Contracts of Database Operations`.

## Building

This is a single maven project divided into submodules.

**NOTE**: `process/postgres` requires a running postgres instance in order for tests to work. Either compile without tests, or create a docker as proposed in the README of the subproject. If you are compiling with tests, be advised that they are quite high level and use the database a lot, so it's a bit slow. Assume the build takes about 5 minutes (2:30 on my system).

## Licensing

This work is licensed under GPL v3.

The output of the tool is explicitly _not_ covered by the license, despite there being an argument that it reuses part of the source code.
