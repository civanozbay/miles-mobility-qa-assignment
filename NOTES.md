# NOTES

Trade-offs, scope decisions, known flakiness, and what I'd change with more time.

## What I chose to skip — and why

- **Login edge cases** (invalid email format, empty input, very long input, Unicode, copy-paste). Listed as Part 2 manual-test categories. They live in `MANUAL_TESTS.md` rather than the automation suite. Most are tagged `auto-later` — worth automating once the framework matures, not in a 4–6 hour budget.
- **Permission-denied path.** I bypassed the location-permission dialog with `setAutoGrantPermissions(true)`. Test scope is core navigation, not permission UX. Dialog text and timing vary across Android versions, which would add flakiness for no scope-relevant signal. The "user denies permission" path is covered as a manual case in Part 2.
- **Stronger app health check.** `assertAppIsRunning` only checks that the app is still in the foreground. It would pass even if an error screen is shown inside the app. I thought about also checking for the Android crash / "App not responding" dialogs, but their IDs change between Android versions and I couldn't test them against a real crash to be sure.
- **Mid-test cookie-popup interception.** The Usercentrics consent popup is dismissed once at `setUp`. If it re-appears mid-test, the framework doesn't intercept it. A clean fix is an app-side bypass via SharedPreferences; but it's out of scope for this submission.
- **Parallel test execution.** Removed the `ThreadLocal<AndroidDriver>` because the suite runs single-threaded against one device/emulator. Easier to defend a simple `static AndroidDriver` than to ship parallelism scaffolding that isn't exercised.
- **CI/CD pipeline.** Not asked for in the brief. Setting up an emulator in CI also adds a lot of extra work. The brief only expects `mvn test` to run locally.

## Known flaky areas

- **`vehicleListButton` is location-dependent.** The test asserts at least one vehicle is rendered. It relies on `adb emu geo fix` succeeding and Berlin having available vehicles at run time. If Miles has zero vehicles in central Berlin during the run, the test fails legitimately — but to an outside reviewer it looks like a framework bug. Documented here so it's not surprising.
- **`acceptCookies()` will fail the test if no popup appears.** The cookie click is not wrapped in `try-catch`. Right now the popup shows on every fresh session, so this is safe. If the app ever stops showing it (or `noReset=true` is set), the setup method throws and TestNG skips the whole suite.
- **`findMeButton` has weak signal.** The find-me FAB just recenters the map; there is no new screen or text I could assert. `assertAppIsRunning` would pass even if the recenter silently failed. Acknowledged in the brief's *"click was accepted"* bar but worth flagging.

## What I'd change with another 4 hours

1. **Capture screenshot, page source and logcat on test failure.** Right now if a test fails I only get a stack trace. For mobile tests that is not enough. I would add a TestNG listener that on failure saves three things: a screenshot of the screen, the Appium page source (the element tree, so I can see if a button was technically there but hidden), and the last 200 lines of `adb logcat` (to catch app-side errors or crashes). Together these make debugging a CI failure much faster.
2. **Stop the cookie popup from appearing at all.** The popup comes from the Usercentrics SDK which stores consent in the app's data. With more time I would pre-write that consent value before each test starts, so the popup never shows. This removes the flakiness instead of working around it. The catch: it needs either a debug build of the app or a rooted device — neither is available when testing the Play Store APK, so it would require app-team cooperation first.
3. **TestNG `IRetryAnalyzer` on `vehicleListButton`.** Single retry only, scoped to the test most affected by external state (vehicle availability). Avoids the *"retry everything"* anti-pattern.
4. **Data-driven negative login with `@DataProvider`.** Right now there is only one negative login test using a single email/password pair from config. With more time I would expand it into a data-driven test using TestNG `@DataProvider` to cover multiple cases (invalid email format, empty inputs, long inputs, special characters) without duplicating the test method.

## AI tools used

I designed and built the framework myself. I used Claude (Anthropic) as a faster reference and a second pair of eyes — the same way I'd use Stack Overflow or a colleague during a focused build. Concretely:

- **Quick lookups for syntax** — the exact `AppiumServiceBuilder` invocation to boot Appium from inside the test process, the `Runtime.exec` argument shape for `adb emu geo fix`.
- **Debugging help** when an error message was ambiguous — mobile: setGeoLocation returning "Unknown mobile command" on UiAutomator2 (which is why I switched to adb emu geo fix), and TestNG's configfailurepolicy=skip behaviour cascading when @BeforeMethod fails.
- **Sanity-checking trade-offs** I had already made up my mind on but wanted a second opinion before committing — dropping `ThreadLocal<AndroidDriver>` because the suite is single-threaded, dropping AssertJ to keep the dependency surface small, access modifiers in `BasePage`, and the cookie-popup handling strategies (including why an app-side bypass isn't realistic for a Play-Store-installed release build).

Where I pushed back on Claude's suggestions:

- Kept the page-object getters returning raw `WebElement` instead of adding `is*Visible()` methods per element — extra noise for no real isolation benefit.
- Reverted a `protected`-everywhere convention in `BasePage` to package-private fields and `public` helpers after the defence of `protected` didn't hold up under scrutiny.
- Made `acceptCookies()` strict (removed the `try-catch`) so the suite fails loudly if the consent flow ever changes silently — flagged as a known risk above.
- Rolled back a `@BeforeClass + terminateApp/activateApp` rewrite of `MapScreenTest` that would have cut suite time but introduced ordering risks.


Every locator, assertion, and config value in this repo is one I can explain and would defend in the follow-up call.
