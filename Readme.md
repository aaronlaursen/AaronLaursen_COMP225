AaronLaursen_COMP225
====================
Comp225 Assignment 1

Fall 2012

This repository contains my first assignment for Comp 225 in Fall 2012 at Macalester College.
This code was created using IntelliJ IDEA - Comunity Edition.

I have created a simple Geohashing app for Android 2.3
For more information on Geohashing, see <http://wiki.xkcd.com/geohashing/Main_Page>.

The user interface is simple and largely non interactive; textViews are used to display various
tdbits of information as it is fetched and calculated by the app, color coded for convenience, with a single button
bellow. This button simply calls up a web browser with GoogleMaps directing the user from their current position to
the geohash for the day. While opening the browser is in some ways less clean than an embedded mapview or similar,
it also doesn't require access to non-core android components (and so should be more cross-platform compatible).

The action is largely contained in the single activity DroidHash, with most of the program running in the onCreate()
method. For the most part, the code is strait-forward; it pulls in the UI elements from main.xml and then dives into
the geohashing propper. Unfortunately, do to emulator issues, the actual gps fetching code is commented out and
some test coordinates are hardcoded. Afterwords, it fetches the most recent Dow Jones opening from
<http://geo.crox.net>. At this point it has all of the needed data, and after some hashing and concatinating, it
produces the destination.

Before begining this small project I completed the example sudoku project and modified some of the features.
Between the two projects, I am reasonably comfortable with the android system. The sudoku project gave me a chance to
play with UI elements and human computer interaction, as well as such things as saving data and playing media files.
This geohashing app was largely created as an excuse to play with the location services and some light internet.