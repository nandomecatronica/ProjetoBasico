package inmetrics.testes;

import inmetrics.core.BasePage;
import inmetrics.core.BaseTest;
import inmetrics.core.DriverFactory;
import inmetrics.pages.MenuPage;
import inmetrics.pages.ResumoPage;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static inmetrics.core.DriverFactory.getDriver;

public class ResumoTest extends BaseTest {
    private MenuPage menuPage = new MenuPage();
    private ResumoPage resumoPage = new ResumoPage();

//    @Test
    public  void teste1ExcluirMovimentacao(){
        menuPage.acessarTelaResumo();
        resumoPage.excluirMovimentacao();

        Assert.assertEquals("Movimentação removida com sucesso!",resumoPage.obterMensagemSucesso());


    }
    @Test(  )
    public void  teste2ResumoMensal(){
        menuPage.acessarTelaResumo();
        Assert.assertEquals("Seu Barriga - Extrato", getDriver().getTitle());

        resumoPage.selecionarAno("2016");
        resumoPage.buscar();

        List<WebElement> elementosEncontrados =
                DriverFactory.getDriver().findElements(By.xpath(".//*[@id='tabelaExtrato']/tbody/tr"));

        Assert.assertEquals(0,elementosEncontrados.size());



    }
}
