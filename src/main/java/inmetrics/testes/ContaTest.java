package inmetrics.testes;

import inmetrics.core.BaseTest;
import inmetrics.core.Propriedades;
import inmetrics.pages.ContasPage;
import inmetrics.pages.MenuPage;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

public class ContaTest extends BaseTest {
    MenuPage menuPage = new MenuPage();
    ContasPage contasPage = new ContasPage();

    @Test
    public void test_1_InserirConta(){
        menuPage.acessarTelaInserirConta();
        contasPage.setNome("Conta do Teste");
        contasPage.salvar();
        Assert.assertEquals("Conta adicionada com sucesso!",contasPage.obterMensagemSucesso());

    }

    @Test
    public  void test_2_AlterarConta(){
        menuPage.acessarTelaListarConta();

        contasPage.clicarAlterarConta("Conta do Teste");
        contasPage.setNome("Conta alterada");
        contasPage.salvar();

        Assert.assertEquals("Conta alterada com sucesso!",contasPage.obterMensagemSucesso());

    }
    @Test
    public void test_3_InseriContaMesmoNome(){
        menuPage.acessarTelaInserirConta();
        contasPage.setNome("Conta mesmo nome");
        contasPage.salvar();

        Assert.assertEquals("Já existe uma conta com esse nome!",contasPage.obterMensagemErro());

    }


}
