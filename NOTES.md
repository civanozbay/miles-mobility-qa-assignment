# NOTES

Trade-offs, scope decisions, known flakiness, and what I'd change with more time.

## What I chose to skip — and why

- **Permission-denied path.** I bypassed the location-permission dialog with `setAutoGrantPermissions(true)`. Test scope is core navigation, not permission UX, so granting upfront removes a step that isn't being verified. The "user denies permission" path is covered as a manual case in Part 2.
- **Stronger app health check.** `assertAppIsRunning` only checks that the app is still in the foreground. It would pass even if an error screen is shown inside the app. I thought about also checking for the Android crash / "App not responding" dialogs, but their IDs change between Android versions and I couldn't test them against a real crash to be sure.
- **Late cookie-popup handling.** The Usercentrics consent popup is dismissed once in `setUp` with a fixed 15s wait. On a slow or low-RAM device the popup can show up later than that and the wait times out. A proper fix would be an app-side bypass (pre-write the consent value) so the popup never appears — out of scope for this submission.
- **Parallel test execution.** Removed the `ThreadLocal<AndroidDriver>` because the suite runs single-threaded against one device/emulator. Easier to defend a simple `static AndroidDriver` than to ship parallelism scaffolding that isn't exercised.
- **CI/CD pipeline.** Not asked for in the brief. Setting up an emulator in CI also adds a lot of extra work. The brief only expects `mvn test` to run locally.
- **TestNG default `configfailurepolicy=skip`.** I set it to `continue` in `testng.xml`. With `skip`, if `@BeforeMethod` fails once, all remaining `@Test` methods in that class are skipped, which hides whether those tests would have passed. With `continue`, each `@Test` runs its own `setUp` and failures stay isolated. Downside: if setUp is truly broken, you wait for the timeout on every test. Fine here because the suite is small.

## Known flaky areas

- **`vehicleListButton` is a smoke test of the button, not of inventory.** The test taps the FAB and accepts either outcome: at least one vehicle rendered, or the empty "no cars" popup. It passes as long as the screen opens correctly. Stricter inventory assertions (counts, specific cars, filtering) are location-dependent — live Berlin inventory changes in real time — so they need test-data control (mocked `/vehicles` endpoint) before they can run reliably. That fix is captured in "What I'd change" #5.
- **`acceptCookies()`** has no try-catch around the consent click, so it depends on the popup appearing. On a fresh session it currently shows every time, so this is safe today. It can break two ways: (1) the popup never appears — only if noReset=true were set, so consent persists across sessions; or (2) on a low-spec run it renders but isn't located within the wait under RAM/CPU contention. Either way, setUp throws a TimeoutException and that test is skipped. Because configfailurepolicy=continue, every test runs its own setUp and is skipped independently — one failure does not cascade across the suite.
- **`findMeButton` has weak signal.** The find-me FAB just recenters the map; there is no new screen or text I could assert. `assertAppIsRunning` would pass even if the recenter silently failed.
- **Map canvas is not inspectable via Appium.** The Google Maps surface renders as a single `android.view.View` with no children in the accessibility tree. UiAutomator2 can locate the element and confirm it is displayed, but cannot inspect anything rendered inside it (vehicle pins, cluster counts, the blue location dot).

## What I'd change with another 4 hours

1. **Capture screenshot on test failure.** A stack trace alone is not enough for mobile. I'd add a TestNG listener that saves a screenshot of the screen when a test fails, so I can see the actual UI state instead of guessing from the stack trace.
2. **Stop the cookie popup from appearing at all.** The popup comes from the Usercentrics SDK which stores consent in the app's data. With more time I would pre-write that consent value before each test starts, so the popup never shows. This removes the flakiness instead of working around it. The catch: it needs either a debug build of the app or a rooted device — neither is available when testing the Play Store APK, so it would require app-team cooperation first.
3. **TestNG `IRetryAnalyzer` on `vehicleListButton`.** Single retry only, scoped to the test most affected by external state (vehicle availability). Avoids the *"retry everything"* anti-pattern.
4. **Data-driven negative login with `@DataProvider`.** Right now there is only one negative login test using a single email/password pair from config. With more time I would expand it into a data-driven test using TestNG `@DataProvider` to cover multiple cases (invalid email format, empty inputs, long inputs, special characters) without duplicating the test method.
5. **Mock backend for inventory-dependent tests.** Run a local proxy (Charles Proxy) returning canned responses for the `/vehicles` endpoint so `vehicleListButton` asserts against a known fixture instead of live Berlin inventory. Removes the location/availability flakiness called out above.

## AI tools used

I designed and built the framework myself. I used Claude as a faster reference and a second pair of eyes. Same way I'd use Stack Overflow or a colleague during a focused build. Concretely:

- **Quick lookups for syntax** — the exact `AppiumServiceBuilder` invocation to boot Appium from inside the test process, the `Runtime.exec` argument shape for `adb emu geo fix`.
- **Debugging help** when an error message was ambiguous — `mobile: setGeoLocation` returning "Unknown mobile command" on UiAutomator2 (which is why I switched to `adb emu geo fix`).
- **Sanity-checking trade-offs** I had already made up my mind on but wanted a second opinion before committing — dropping `ThreadLocal<AndroidDriver>` because the suite is single-threaded, considering AssertJ and reject to keep the dependency surface small, access modifiers in `BasePage`, and the cookie-popup handling strategies (including why an app-side bypass isn't realistic for a Play-Store-installed release build).
- **README and NOTES wording.** Iterated phrasing with Claude — I'd write a rough version, ask for a tighter rewrite, then edit the result.

Where I pushed back on Claude's suggestions:

- Reverted a `protected`-everywhere convention in `BasePage` to package-private fields and `public` helpers after the defence of `protected` didn't hold up under scrutiny.
- Made `acceptCookies()` strict (removed the `try-catch`) so the suite fails loudly if the consent flow ever changes silently — flagged as a known risk above.
- Rolled back a `@BeforeClass + terminateApp/activateApp` rewrite of `MapScreenTest` that would have cut suite time but introduced ordering risks.


Every locator, assertion, and config value in this repo is one I can explain and would defend in the follow-up call.
