# Miles Mobility — QA Take-Home

Appium + TestNG framework covering negative login and map-screen smoke for the Miles Mobility Android app.

## What the suite runs

- **`LoginTest.negativeLogin`** — enters a made-up email + wrong password, asserts the error message
- **`MapScreenTest`** — 5 buttons on the map (hamburger, help, filter, vehicle list, find-me): visibility + click + app stays in `com.driveby.app`

Each test starts a fresh Appium session against a single emulator/device.

## Prerequisites

| Tool | Version                                  |
|------|------------------------------------------|
| JDK | 17                                       |
| Maven | 3.6+                                     |
| Node.js | 18+                                      |
| Android SDK + `platform-tools` | `adb` must be runnable from the terminal |
| Appium | 2.x                                      |
| Appium UiAutomator2 driver | latest                                   |

Install Appium and the driver once:

```bash
npm install -g appium
appium driver install uiautomator2
```

Project-specific setup:
- Create an AVD called **`Pixel_3`** (or change `avd.name` in `config.properties` to match yours).
- Install the **Miles Mobility** app on that AVD from the Play Store.
- The framework boots its own Appium server via `AppiumServiceBuilder` — do **not** start `appium` manually beforehand.

## Run

From the project root:

```bash
mvn test
```

That single command:
1. Starts an embedded Appium 2.x server on `127.0.0.1:4723`
2. Launches the AVD named in `config.properties` (skipped if `emulator=false`)
3. Mocks the device location to Berlin (Miles service area) via `adb emu geo fix`
4. Runs the TestNG suite
5. Shuts Appium down on suite end

### Run a subset

```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=MapScreenTest#helpButton
```

## Test data

Test credentials live in `src/test/resources/config.properties`. They are throwaway by design (the brief only requires a negative login, so any made-up values work) and are **not** hard-coded in Java sources.

Override at runtime without editing the file:

```bash
mvn test -Dtest.email=foo@example.com -Dtest.password=whatever
```

## Configuration (`src/test/resources/config.properties`)

| Key | Default | Purpose |
|-----|---------|--------|
| `appium.url` | `http://127.0.0.1:4723` | Embedded Appium server URL (host + port parsed from this) |
| `platform.name` | `Android` | |
| `automation.name` | `UiAutomator2` | |
| `device.name` | `emulator-5554` | ADB device id. Sent to Appium as a capability and used by `adb emu geo fix`. On a real device this must match the id shown by `adb devices`. |
| `emulator` | `true` | When `true`, the suite auto-launches the AVD via `avd.name` |
| `avd.name` | `Pixel_3` | AVD Manager name; must match exactly |
| `app.package` | `com.driveby.app` | Miles app package |
| `app.activity` | `com.driveby.app.MainActivity` | Launcher Activity |
| `implicit.wait` | `2` | seconds |
| `explicit.wait` | `15` | seconds — used by `WebDriverWait` |
| `location.mock` | `true` | When `true`, runs `adb emu geo fix` after driver init |
| `location.latitude` | `52.5200` | Berlin |
| `location.longitude` | `13.4050` | |

## Running against a physical device

1. Enable USB debugging on the phone and plug it in.
2. Find the device id:
   ```bash
   adb devices
   ```
3. Edit `config.properties`:
   ```properties
   emulator=false
   device.name=<id from adb devices>
   location.mock=false
   ```
   `adb emu geo fix` only works on emulators. On a real device the phone's actual GPS is used — make sure you're inside a Miles service area or the map will show "out of service area".

## Reporting

TestNG output lives in `target/surefire-reports/` (`index.html` for a basic HTML view).

## Notes

- See `NOTES.md` for what was intentionally skipped, known flaky areas, what would change with another 4 hours, and AI tool disclosure.
- See `MANUAL_TESTS.md` for the Part 2 manual test cases that complement this automation.
