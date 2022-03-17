package inmetrics.testes;

import inmetrics.core.BaseTest;
import inmetrics.core.Propriedades;
import inmetrics.pages.ContasPage;
import inmetrics.pages.MenuPage;
import org.junit.Assert;
import org.junit.Test;

public class RemoverMovimentacaoContaTest extends BaseTest {
    MenuPage menuPage = new MenuPage();
    ContasPage contasPage = new ContasPage();

    @Test
    public void  testExcluirContaComMovimentacao(){
        menuPage.acessarTelaListarConta();
        contasPage.clicarExcluirConta("Conta com movimentacao");
        Assert.assertEquals("Conta em uso na movimentações",contasPage.obterMensagemErro());

    }
}
