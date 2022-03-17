package inmetrics.testes;

import inmetrics.core.BaseTest;
import inmetrics.core.Propriedades;
import inmetrics.pages.HomePage;
import inmetrics.pages.MenuPage;
import org.junit.Assert;
import org.junit.Test;

public class SaldoTest extends BaseTest {
    HomePage homePage = new HomePage();
    MenuPage menuPage = new MenuPage();

    @Test
    public void testSaldoConta(){
        menuPage.acessarTelaPrincipal();
        Assert.assertEquals("534.00",homePage.obterSaldoConta("Conta para saldo"));
    }
}
