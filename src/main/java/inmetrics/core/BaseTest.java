package inmetrics.core;

import inmetrics.pages.LoginPage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;

import static inmetrics.core.DriverFactory.getDriver;
import static inmetrics.core.DriverFactory.killDriver;

public class BaseTest {
    @Rule
    public TestName testName = new TestName();

    private LoginPage page = new LoginPage();

    @Before
    public void Inicializa(){
        page.acessarTelaInicial();
        page.setEmail("wagner@costa");
        page.setSenha("123456");
        page.entrar();
    }

    @After
    public void Finaliza() throws IOException {

        TakesScreenshot ss = (TakesScreenshot) getDriver();
        File arquivo = ss.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(arquivo, new File("target" + File.separator + "screenshot"+
                File.separator + testName.getMethodName() + ".jpg"));

            if(Propriedades.FECHAR_BROWSER){
                killDriver();
            }

    }


}
