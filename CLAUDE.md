# Simpletask (maintained fork)

Fork of the archived mpcjanssen/simpletask-android, kept working on current Android.
Default branch: `maintenance/modernize`. Upstream's `master` is kept untouched for reference.

## Rules

- **Maintenance mode.** No new features and no UI changes. Only fix what is broken or what new
  Android versions force. List any unavoidable behaviour change in the commit message.
- **Keep the original structure.** Don't reorganize packages, classes or patterns. Prefer the
  smallest fix in the existing code.
- **Small, iterative commits.** One purpose per commit, and every commit builds and passes tests.
  Upgrade in steps, not big jumps.
- Prefer reusing maintenance fixes from other forks (cherry-pick with `-x`, keep the original
  author) over writing new code.
- **Adversarial review of every change.** Before a change counts as done, a separate reviewer
  agent checks it: a skeptical Android expert who is protective of the app, as if the original
  author were reviewing. It gets the diff, the purpose and these rules, may build the app and try
  it in the emulator (fake data only, never the maintainer's phone or real Dropbox). Its objections
  are not taken at face value: whoever made the change verifies each one against the code or the
  emulator, then fixes it or rejects it with a stated reason, before merging. The reviewer also
  looks for regressions in visual and rendering performance (jank, dropped frames, extra layout
  passes or overdraw), e.g. by comparing `dumpsys gfxinfo` frame stats before and after.
- Ship the `dropbox` and `cloudless` flavors. Keep `nextcloud`, `webdav` and `encrypted` building,
  but they are not released.

## Build

- JDK 21 (`JAVA_HOME=/opt/homebrew/opt/openjdk@21` on the maintainer's Mac). Android SDK from
  Homebrew's `android-commandlinetools`.
- `./gradlew assembleDropboxDebug assembleCloudlessDebug testDropboxDebugUnitTest testCloudlessDebugUnitTest`
- The build reads git with JGit, which fails on a detached HEAD in a git worktree. Build on a branch.
- Debug builds add `.debug` to the package ID and include LeakCanary.

## Secrets (never commit)

- Dropbox keys go in `local.properties`: `dropbox.appKey` (Full Dropbox app). `dropbox.folderAppKey`
  (App folder app) is not configured, so that login option doesn't work in our builds.
- Release signing: the keystore is kept by the maintainer (KeePass backup). CI reads it from the
  `SIGNING_*` GitHub secrets. Losing the key means users must reinstall, so never regenerate it.

## Releasing

Push a tag `v<upstream version>-fork.<n>` (e.g. `v11.0.2-fork.2`). CI builds signed release APKs and
attaches them to a GitHub Release.

## Watch for

- Android raising its minimum target SDK for installs: bump `targetSdk`/`compileSdk` as a small step.
- Dependabot opens monthly grouped PRs; merge them once CI passes.
