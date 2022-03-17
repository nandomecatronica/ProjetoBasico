package inmetrics.testes;

import inmetrics.core.BaseTest;
import inmetrics.core.Propriedades;
import inmetrics.pages.MenuPage;
import inmetrics.pages.MovimentacaoPage;
import inmetrics.utils.DataUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static inmetrics.utils.DataUtils.*;

public class MovimentacaoTest extends BaseTest {
    private MenuPage menuPage = new MenuPage();
    private MovimentacaoPage movPage = new MovimentacaoPage();

    @Test
    public void test1InserirMovimentacao(){
        menuPage.acessarTelaInserirMovimentacao();

        movPage.setDataMovimentacao(obterDataFormatada(new Date()));
        movPage.setDataPagamento(obterDataFormatada(new Date()));
        movPage.setDescricao("Movimentção de teste");
        movPage.setInteressado("Nome interessado");
        movPage.setValor("200");
        movPage.setConta("Conta com movimentacao");
        movPage.setStatusPago();
        movPage.salvar();

        Assert.assertEquals("Movimentação adicionada com sucesso!",movPage.obterMensagemSucesso());

    }

    @Test
    public void test2CamposObrigatorios(){
        menuPage.acessarTelaInserirMovimentacao();
        movPage.salvar();

        List<String> erros = movPage.obterErros();
        Assert.assertTrue(erros.containsAll(Arrays.asList(
                                            "Data da Movimentação é obrigatório",
                                            "Data do pagamento é obrigatório",
                                            "Descrição é obrigatório",
                                            "Interessado é obrigatório",
                                            "Valor é obrigatório",
                                            "Valor deve ser um número")));

        Assert.assertEquals(6,erros.size());

    }

    @Test
    public void  test3InserirMovimentacaoFutura(){
        menuPage.acessarTelaInserirMovimentacao();

        Date dataFutura = obterDataComDiferencaDias(20);

        movPage.setDataMovimentacao(obterDataFormatada(dataFutura));
        movPage.setDataPagamento(obterDataFormatada(dataFutura));
        movPage.setDescricao("Movimentação futura de teste");
        movPage.setInteressado("Nome interessado");
        movPage.setValor("300");
        movPage.setConta("Conta com movimentacao");
        movPage.setStatusPago();
        movPage.salvar();

        List<String> erros = movPage.obterErros();
        Assert.assertTrue(erros.contains("Data da Movimentação deve ser menor ou igual à data atual"));

        Assert.assertEquals(1,erros.size());


    }


}
