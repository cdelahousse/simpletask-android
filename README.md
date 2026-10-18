Simpletask
==========

> **About this fork.** [mpcjanssen/simpletask-android](https://github.com/mpcjanssen/simpletask-android)
> by Mark Janssen was archived in 2023. This fork keeps it building and working on current Android
> (tested on Android 17) in **maintenance mode**: no new features or UI changes, only fixes needed to
> keep the app working. All credit for the app goes to the original author; it remains GPL-3.0.
>
> * **Download:** signed APKs on the [Releases page](https://github.com/cdelahousse/simpletask-android/releases).
>   They use a new package ID (`ca.delahousse.simpletask…`), so they install alongside, not over, the original app.
> * **Flavors built:** `dropbox` and `cloudless` (use cloudless with Syncthing-Fork (from F-Droid) for Syncthing).
> * **Dropbox:** log in with *Full Dropbox* access; the *App folder* option is not configured in these builds.
> * **What changed:** see the commit history on the default branch. In short: dead build dependencies replaced,
>   toolchain and target SDK updated, edge-to-edge layout fixes, Dropbox SDK 7.0.0 (fixes login),
>   notification permission on Android 13+.

Simpletask is a simple task manager for Android, based on the brilliant [todo.txt](http://todotxt.com) format by [Gina Trapani](http://ginatrapani.org/).

  * [Documentation](#documentation)
  * [Translation](#translation)
  * [Cloudless Version](#cloudless-version)
  * [Nextcloud Version](#nextcloud-version)
  * [WebDAV Version](#webdav-version)
  * [Dropbox](#dropbox)

## Documentation

See documentation [here](./app/src/main/assets/index.en.md).

## Translation

Simpletask is translated using weblate: <a href="https://hosted.weblate.org/engage/simpletask/?utm_source=widget">
                                        <img src="https://hosted.weblate.org/widgets/simpletask/-/svg-badge.svg" alt="Translation status" />
                                        </a>

## Cloudless Version

<a href="https://f-droid.org/repository/browse/?fdid=nl.mpcjanssen.simpletask" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80"/></a>


Cloudless stores tasks in a todo.txt file on the device. A separate app (such as Syncthing) can be used to Sync the file.


Because the todo.txt file could be anywhere on the device to allow sync by a different app, the Cloudless version
requests full storage access. This is unfortunately the only way to make common use case work while staying sane.

## Nextcloud Version

<a href="https://f-droid.org/repository/browse/?fdid=nl.mpcjanssen.simpletask.nextcloud" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80"/></a>


Nextcloud stores tasks in a todo.txt file using Nextcloud.

## WebDAV Version

<a href="https://f-droid.org/repository/browse/?fdid=nl.mpcjanssen.simpletask.webdav" target="_blank">
<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="80"/></a>


Nextcloud stores tasks in a todo.txt file on any WebDAV server.

## Dropbox

The Dropbox version is not available from F-Droid. You can download builds of the different versions from: https://mpcjanssen.nl/artifacts/
