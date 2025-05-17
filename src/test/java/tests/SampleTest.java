import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class SampleTest {

    @Test
    @Description("Verify something important")
    public void testOne() {
        stepOne();
    }

    @Step("Step one executed")
    public void stepOne() {
        assert true;
    }
}
