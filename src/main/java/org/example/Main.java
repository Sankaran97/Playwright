package org.example;
import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ScreenshotCaret;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.setDefaultAssertionTimeout;
import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {

    public void accessibility_testing(Page page){
        AxeResults accessbilityresults = new AxeBuilder(page).analyze();
        assertEquals(Collections.emptyList(), accessbilityresults.getViolations());
    }

    public void locators_with_input_textbox(Page page){
        page.navigate("https://letcode.in/edit");
        page.getByText("Work-Space").click();
        page.getByText(" Edit ").click();
        page.getByPlaceholder("Enter first & last name").fill("Sankar");
        Locator locator = page.locator("#join");
        locator.click();
        locator.press("End");
        locator.type(" Man");
        locator.press("Tab");
        System.out.println(page.locator("id=getMe").getAttribute("value"));
        page.locator("#clearMe").clear();
    }

    public void locators_with_dropdown(Page page){
        page.navigate("https://letcode.in/dropdowns");
        Locator locator = page.locator("#fruits");
        locator.selectOption("2");
        locator.selectOption(new SelectOption().setLabel("Apple"));
        locator.selectOption(new SelectOption().setIndex(1));
        System.out.println(page.locator("//p[@class='subtitle']").textContent());
        Locator mul = page.locator("id=superheros");
        mul.selectOption(new String[] {"am", "im", "sm"});
        Locator mulop = page.locator("#lang");
        Locator options = mulop.locator("option");
        int count = options.count();
        System.out.println(count);
        mulop.selectOption(new SelectOption().setIndex(count-1));
        Locator ind = page.locator("#country");
        ind.selectOption("India");
        String selected=ind.inputValue();
        System.out.println(selected);
        System.out.println();
    }

    public void codeGen(Page page){
        //Command to execute in terminal
        //mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen letcode.in"
        //Generates the below code as per the navigation
        page.navigate("https://letcode.in/");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Work-Space")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Drop-Down")).click();
        page.locator("#fruits").selectOption("3");
        page.locator("#superheros").selectOption("am");
        page.locator("#superheros").selectOption("ta");
        page.locator("#superheros").selectOption("dd");
        page.locator("#lang").selectOption("py");
        page.locator("#country").selectOption("India");
    }

    public void alerts(Page page){
        page.navigate("https://letcode.in/alert");
        //alerts can be dismissed by playwright if we didn't accept by default
        //Consumer class is used to turn off an Event handler to accomodate a new Event handler
        Consumer<Dialog> alert = new Consumer<Dialog>() {
            @Override
            public void accept(Dialog dialog) {
                System.out.println(dialog.message());
                page.offDialog(this);
            }
        };
        page.locator("#accept").click();
        page.onDialog(dialog -> {
            System.out.println(dialog.defaultValue()); // if there is any value in prompt by default it returns the value
            System.out.println(dialog.message()); //it returns alert/prompt text
            dialog.accept("Sankaran");
        });

        page.locator("#prompt").click();
        System.out.println(page.locator("#myName").textContent());
    }

    public void httpAuthentication(Page page){
        page.navigate("https://the-internet.herokuapp.com/basic_auth");
        System.out.println(page.locator("h3").textContent());
    }

    public void browserContext(Page page, Browser browser){
        page.navigate("https://opensource-demo.orangehrmlive.com/");
        page.locator("//input[@name='username']").fill("Admin");
        page.locator("//input[@name='password']").fill("admin123");
        page.locator("//button[@type='submit']").click();
        String name = page.locator("//p[@class='oxd-userdropdown-name']").textContent();
        System.out.println(name);
        //Like opening an incognito we are using new context to start fresh by having separate cache and memory
        BrowserContext context2 = browser.newContext();
        Page newpage = context2.newPage();
        newpage.navigate("https://opensource-demo.orangehrmlive.com/");
        //will throw error as there is no cache used for default login while loading website
        System.out.println(newpage.locator("//p[@class='oxd-userdropdown-name']").textContent());
    }

    public void recordVideo(Page page){
        page.navigate("https://opensource-demo.orangehrmlive.com/");
        page.locator("//input[@name='username']").fill("Admin");
        page.locator("//input[@name='password']").fill("admin123");
        page.locator("//button[@type='submit']").click();
        String name = page.locator("//p[@class='oxd-userdropdown-name']").textContent();
        System.out.println(name);
    }

    public void traceViewer(Page page, BrowserContext context){
        //command to view trace in local
        //mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace trace.zip"
        //command to run and show source in trace
        //PLAYWRIGHT_JAVA_SRC="src/main/java/" mvn test -Ptrace //and set pom.xml profile section
        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
        page.navigate("https://opensource-demo.orangehrmlive.com/");
        page.locator("//input[@name='username']").fill("Admin");
        page.locator("//input[@name='password']").fill("admin123");
        page.locator("//button[@type='submit']").click();
        String name = page.locator("//p[@class='oxd-userdropdown-name']").textContent();
        System.out.println(name);

        context.tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("trace.zip"))
        );
    }

    public void getbylocators(Page page){
        page.navigate("https://opensource-demo.orangehrmlive.com/");
        page.getByPlaceholder("Username").fill("Admin");
        page.getByPlaceholder("Password").fill("admin123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        //page.locator("//i[@class='oxd-icon bi-chevron-right']").click();
        page.getByPlaceholder("Search").fill("ma");
        page.getByRole(AriaRole.LISTITEM).first().click(); //last() or //nth(2) index specific
        page.getByAltText("client brand banner").click();
    }

    public void otherLocators(Page page){
        page.navigate("https://letcode.in/test");
        page.click("'Click'");
        page.locator("text=Click");
        page.locator("button:has-text('Goto Home')").click();
        page.locator("nav:text('Products')").click();
        page.navigate("http://127.0.0.1:5500/allText/index.html");
        page.locator("button:btn btn-primary:visible").click(); //CSS
        page.locator("//button[@class='btn btn-primary'] >> visible=true").click(); //xpath
        page.locator("button").locator("nth=1").click();
        System.out.println(page.locator("#attach").textContent());
        page.locator("button").locator("nth=-1").click();
        System.out.println(page.locator("#attach").textContent());

        page.locator("#innerText, #innerText2").click();
        page.locator("//button[text()='Inner Text'] | //button[text()='Inner Text 1']").click();

    }

    public void debugandInspector(Page page){
        //command to execute
        //PWDEBUG=1 PLAYWRIGHT_JAVA_SRC=src/main/java mvn test -Pdebug
        //add profile in pom.xml file
        page.navigate("https://letcode.in/");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Work-Space")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Drop-Down")).click();
        page.pause();
        page.locator("#fruits").selectOption("3");
        page.locator("#superheros").selectOption("am");
        page.locator("#superheros").selectOption("ta");
        page.locator("#superheros").selectOption("dd");
        page.locator("#lang").selectOption("py");
        page.locator("#country").selectOption("India");
    }

    public void frames(Page page){
        page.navigate("https://letcode.in/frame");
        //way 1
        Frame frame = page.frame("firstFr");
        frame.getByPlaceholder("Enter name").fill("say my name");
        frame.getByPlaceholder("Enter email").fill("Coolie");
        //way 2 - where innerframe is a linktext
        Frame secframe = page.frameByUrl(Pattern.compile("innerframe"));
        //way 3 - link is provided completely
        Frame thirdframe = page.frameByUrl("https://letcode.in/innerframe");
        secframe.getByPlaceholder("Enter email").fill("Powerhouse");
        //To get child frame
        List<Frame> childframes = frame.childFrames();
        System.out.println(childframes.size());
        childframes.forEach(ch->{
            System.out.println(ch.url());
        });
        //To get all frames in a page
        List<Frame> frames = page.frames();
        System.out.println(frames.size());
        //New version of locating frames
        FrameLocator newframe = page.frameLocator("#firstFr");
        newframe.getByPlaceholder("Enter email").fill("New");
        FrameLocator chiframe = newframe.frameLocator("//div[contains(@class,'container has-text-centered')]//iframe[1]");
        chiframe.getByPlaceholder("Enter email").fill("close");
    }

    public void downloadfile(Page page){
        page.navigate("https://letcode.in/file");
        Download download = page.waitForDownload(()->{
            page.locator("'Download Text'").click();
        });
        System.out.println(download.path());
        System.out.println(download.url());
        System.out.println(download.failure());
        System.out.println(download.suggestedFilename());
        download.saveAs(Paths.get(download.suggestedFilename()));
    }

    public void uploadfile(Page page){
        page.navigate("https://the-internet.herokuapp.com/upload");
        FileChooser fileChooser = page.waitForFileChooser(()->{
            page.locator("#drag-drop-upload").click();
        });
        if(fileChooser.isMultiple()){
            fileChooser.setFiles(new Path[]{
                    Paths.get("sample.txt"),
                    Paths.get("sec.txt"),
                    Paths.get("/Users/sankaran/Downloads/third.txt")
            });
        }
    }

    public void screenshot(Page page){
        page.navigate("https://www.amazon.in/");
        //current screen shot - full size
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("./snaps/img.png")));
        //Entire page - full scroll from top to bottom of the page
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("./snaps/full.png")).setFullPage(true));
        //Locator specific screenshot
        Locator search = page.getByPlaceholder("Search Amazon.in");
        search.click();
        search.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("./snaps/searchboxinitial.png")).setCaret(ScreenshotCaret.INITIAL));
        search.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("./snaps/searchboxhide.png")).setCaret(ScreenshotCaret.HIDE));
        //mask locator for data privacy
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("./snaps/mask.png")).setCaret(ScreenshotCaret.INITIAL).setMask(Arrays.asList(search)));
    }

    public void windowhandle(Page page){
        page.navigate("https://letcode.in/window");
        Page popup = page.waitForPopup(()->{
            page.locator("#home").click();
        });
        popup.waitForLoadState();
        System.out.println(popup.title());
        System.out.println(popup.url());
        page.waitForPopup(new Page.WaitForPopupOptions().setPredicate(
                p-> p.context().pages().size() == 3),()->{
            page.locator("#multi").click();
        });
        List<Page> pages = page.context().pages();
        for(Page tabs: pages){
            System.out.println(tabs.title());
        }
        Page alertpage = pages.get(1);
        Page droppage = pages.get(2);
        System.out.println(alertpage.textContent("h1")+"\n"+droppage.textContent("h1"));
    }

    public void multipleElements(Page page){
        page.navigate("https://letcode.in/elements");
        page.locator("//input[@name='username']").fill("ortonikc");
        page.locator("#search").click();
        Locator links = page.locator("//div[@class='content']/p/a");
        links.last().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(20000));
        int count = links.count();
        System.out.println(count);
        for(int i =0;i<count;i++){
            System.out.println(links.nth(i).textContent());
        }
    }

    public void assertfun(Page page){
        page.navigate("https://letcode.in/edit");
        assertThat(page).hasTitle("Edit Fields | LetCode with Koushik");
        assertThat(page).hasURL("https://letcode.in/edit");
        assertThat(page.locator("#join")).hasValue("I am good");
        assertThat(page.locator("#join")).not().hasValue("I am good boy");
        //by default exception will throw within 5 seconds but we can set time by below format
        setDefaultAssertionTimeout(20000);
        //only hard assert in playwright, no soft assert
    }

    public void skiplogin(Page page, BrowserContext context){
        page.navigate("https://opensource-demo.orangehrmlive.com/");
//        page.locator("//input[@name='username']").fill("Admin");
//        page.locator("//input[@name='password']").fill("admin123");
//        page.locator("//button[@type='submit']").click();
        String name = page.locator("//p[@class='oxd-userdropdown-name']").textContent();
        System.out.println(name);
        //generate auth
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("auth.json")));
    }

    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //for http authentication use below code
//        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setHttpCredentials("admin",
//                "admin"));
        //To record video of execution
//        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/"))
//                .setRecordVideoSize(1280,720));
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("auth.json")));
//        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        Main m = new Main();
        m.skiplogin(page, context);
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
}