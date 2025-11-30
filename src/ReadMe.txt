Playwright
    Invented by microsoft in 2020

Why use Playwirght:
    Free and Open source
    Multibrowser support - Chromium, Firefox, Webkit(MacOS browser engine which supports Safari)
    Multiplatform support - Linux, IOS, Windows
    MultiTab support - Run test across multiple tabs and allows multiple user tests
    Prevent Flaky test:
         Auto Wait: Selenium waits for particular seconds for an element to be visible but playwright waits
                    for the element till it has become actionable to execute actions on it
         Dynamic web assertion: In most of the scenarios elements become dynamic in nature, playwright provides
                    dynamic assertion to overcome this situation
         Tracing:  Playwright provides logs, screenshots, and videos of flaky tests to eliminate them completely
    Authentication save: testers need to create multiple login scenarios for multiple tests. These take extra
                    time and extra steps for test execution. With Playwright the authentication state can be
                    saved and reused with subsequent tests to skip this overload completely.
    Parallel testing
    Faster execution

Limitation of Playwright:
    Limited language support: Python, Java, TypeScript/Javascript, .Net
    Not fit for native application: Only Web and mobile web browser -> No mobile apps or Desktop apps
    No support for IE11 - as it is depricated by microsoft

Architecture of Playwright framework:
    Tests <--Web-socket--> Playwright
    Selenium uses HTTP methods for each test to establish a connection to server and terminates at the end
    But in playwright a websocket is established to server and terminates when all the test is completed
    this saves time in execution

Types of testing:
    End-to-End testing
    API testing
    Cross browser testing
    Functional testing
    Unit testing

