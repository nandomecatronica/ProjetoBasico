package inmetrics.Suites;

import inmetrics.core.DriverFactory;
import inmetrics.pages.LoginPage;
import inmetrics.testes.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import inmetrics.core.BasePage;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContaTest.class,
        MovimentacaoTest.class,
        RemoverMovimentacaoContaTest.class,
        SaldoTest.class,
        ResumoTest.class

})
public class SuiteGeral {
    private static LoginPage page = new LoginPage();

    @BeforeClass
    public static void reset(){
        page.acessarTelaInicial();
        page.setEmail("wagner@costa");
        page.setSenha("123456");
        page.entrar();
        page.resetar();

        DriverFactory.killDriver();
    }


}
