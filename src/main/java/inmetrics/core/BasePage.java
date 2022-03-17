package inmetrics.core;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static inmetrics.core.DriverFactory.getDriver;

public class BasePage {
    /*********** TextField e TextArea *****/

    public void escrever(By by, String texto) {
        getDriver().findElement(by).clear();
        getDriver().findElement(by).sendKeys(texto);
    }

    public void escrever(String id_campo, String texto) {
        escrever(By.id(id_campo),texto);
    }

    public String obterValorCampo(String id_campo) {
        return getDriver().findElement(By.id(id_campo)).getAttribute("value");
    }

    public void clicarRadio(By by) {
        getDriver().findElement(by).click();
    }

    public void clicarRadio(String id) {
        clicarRadio(By.id(id));
    }

    public boolean isRadioMarcado(String id) {
        return getDriver().findElement(By.id(id)).isSelected();
    }

    public void clicarCheck(String id) {
        getDriver().findElement(By.id(id)).click();
    }

    public void isCheckMarcado(String id) {
        getDriver().findElement(By.id(id)).isSelected();
    }

    public void selecionarCombo(String id, String valor) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        combo.selectByVisibleText(valor);
    }

    public void deselecionarCombo(String id, String valor) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        combo.selectByVisibleText(valor);
    }

    public List<String> obterValorCombo(String id) {
        WebElement element = getDriver().findElement(By.id(id));
        Select combo = new Select(element);
        return Collections.singletonList(combo.getFirstSelectedOption().getText());
    }

    public List<String> obterValoresCombo(String id) {
        WebElement element = getDriver().findElement(By.id("elementosForm:esportes"));
        Select combo = new Select(element);
        List<WebElement> allSelectedOptions = combo.getAllSelectedOptions();
        List<String> valores = new ArrayList<String>();
        for (WebElement opcao : allSelectedOptions) {
            valores.add(opcao.getText());
        }
        return valores;
    }

    public void clicarBotao(By by) {
        getDriver().findElement(by).click();
    }
    public void clicarBotao(String id) {
        clicarBotao(By.id(id));
    }
    public  void clicarBotaoPorTexto(String texto){
        clicarBotao(By.xpath("//button[.='" + texto + "']"));
    }

    public String obterValueElemento(String id) {
        return getDriver().findElement(By.id(id)).getAttribute("value");
    }

    public void clicarLink(String link) {
        getDriver().findElement(By.linkText(link)).click();
    }

    public String obterTexto(By by) {
        return getDriver().findElement(by).getText();
    }

    public String obterTexto(String id) {
        return obterTexto(By.id(id));
    }

    public String alertaObterTexto() {

        Alert alert = getDriver().switchTo().alert();
        return alert.getText();
    }

    public String alertaObterTextoEAceita() {
        Alert alert = getDriver().switchTo().alert();
        String valor = alert.getText();
        alert.accept();
        return valor;
    }

    public String alertaObterTextoENega() {
        Alert alert = getDriver().switchTo().alert();
        String valor = alert.getText();
        alert.dismiss();
        return valor;
    }


    public void alertaEscrever(String valor) {
        Alert alert = getDriver().switchTo().alert();
        alert.sendKeys(valor);
        alert.accept();
    }

    public void entrarFrame(String id) {
        getDriver().switchTo().frame(id);
    }

    public void trocarJanela(String id) {
        getDriver().switchTo().window(id);
    }

    public void sairFrame() {
        getDriver().switchTo().defaultContent();
    }

    public int obterQuantidadeOpcoesCombo(String id) {
        WebElement element = getDriver().findElement(By.id(id));
        Select cmb = new Select(element);
        List<WebElement> options = cmb.getOptions();
        return options.size();
    }

    public boolean verificarOpcaoCombo(String id, String opcao) {
        WebElement element = getDriver().findElement(By.id(id));
        Select cmb = new Select(element);
        List<WebElement> options = cmb.getOptions();
        for (WebElement option : options) {
            if(option.getText().equals(opcao)){
                return true;
            }
        }
        return false;
    }

    public void selecionarComboPrime(String radical, String idValor){
        clicarRadio(By.xpath("/html/body/div[1]/div[5]/div[2]/div/form/div/div[" + radical + "]/div/div[3]/span"));
        clicarRadio(By.id(idValor));
    }

    /****************JS*******************/
    public Object executarJS(String cmd,Object...param){

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
//        js.executeScript("alert('Testando js via selenium')");
//        js.executeScript("document.getElementById('elementosForm:nome').value = 'Escrita via js'");
        return js.executeScript(cmd,param);
    }


    /****************Tabela*******************/
    public  WebElement obterCelula(String colunaBusca,String valor,String colunaBotao, String botao){
        //procurar coluna do registro
        WebElement tabela = getDriver().findElement(By.xpath("//*[@id='"+ botao + "']"));
        int idColuna = obterIndiceColuna(colunaBusca,tabela);

        //encontrar linha do registro
        int idLinha = obterIndiceLinha(valor, tabela, idColuna);

        //procurar coluna do botao
        int idColunaBotao = obterIndiceColuna(colunaBotao,tabela);

        //clicar botao da celula encontrada
        WebElement celula = tabela.findElement(By.xpath(".//tr["+idLinha+"]/td["+idColunaBotao+"]"));
        return celula;

    }

    public  void clicarBotaoTabela(String colunaBusca,String valor,String colunaBotao, String idTabela){

        WebElement celula = obterCelula(colunaBusca,valor,colunaBotao,idTabela);
        celula.findElement(By.xpath(".//input")).click();

    }

    protected int obterIndiceLinha(String valor, WebElement tabela, int idColuna) {
        List<WebElement> linhas = tabela.findElements(By.xpath("./tbody/tr/td[" + idColuna +"]"));
        int idLinha = -1;
        int registros = 0;
        for(int i = 0; i < linhas.size(); i++) {
            if (linhas.get(i).getText().equals(valor)) {
                registros = registros +1;
                idLinha = i + 1;
//                break;
            }
        }
        if (registros>1){
            System.out.println("Existem "+ registros + " registros com a descrição " + valor + " informado.");
        }
        return idLinha;
    }


    protected int obterIndiceColuna(String coluna, WebElement tabela) {
        List<WebElement> colunas = tabela.findElements(By.xpath(".//th"));
        int idColuna = -1;
        for(int i = 0; i < colunas.size(); i++){
            if(colunas.get(i).getText().equals(coluna)){
                idColuna = i+1;
                break;
            }
        }
        return  idColuna;
    }

}
