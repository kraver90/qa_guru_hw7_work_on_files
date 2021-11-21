package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import io.qameta.allure.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;

import java.io.*;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static guru.qa.TestData.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilesTest extends TestBase {
    @Test
    @Owner("Кочуров Д.Е.")
    @Feature("Работа с файлами")
    @DisplayName("Имя файла отображается после загрузки")
    void filenameShouldDisplayedAfterUploadActionFromClasspathTest() {
        open(UPLOAD_PAGE_FILE_UPLOAD);
        $(".featured-btn").click();
        $("input[id = 'file_0']").uploadFromClasspath(UPLOAD_FILE);
        $(".uploadfile").shouldHave(text(UPLOAD_FILE));
        $(".btn-success").click();
        $(".upFile-info").shouldHave(text(UPLOAD_FILE));
    }

    @Test
    @Owner("Кочуров Д.Е.")
    @Feature("Работа с файлами")
    @DisplayName("Скачивание текстового файла и проверка его содержимого")
    void downloadSimpleFileTest() throws IOException {
        open(DOWNLOAD_FILE_TXT);
        File download = $("#downloadButton").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains(TXT_RESULT));
    }

    @Test
    @Owner("Кочуров Д.Е.")
    @Feature("Работа с файлами")
    @DisplayName("Скачивание PDF файла")
    void pdfFileDownloadTest() throws IOException {
        open(DOWNLOAD_FILE_PDF);
        File pdf = $(".left").download();
//        Проверка на кол-во страниц в pdf файле
//        PDF parsedPdf = new PDF(pdf);
//        Assertions.assertEquals(190, parsedPdf.numberOfPages);
//        Проверка по тексту
        assertThat(new PDF(pdf)).containsExactText(PDF_RESULT);
    }

    @Test
    @Owner("Кочуров Д.Е.")
    @Feature("Работа с файлами")
    @DisplayName("Скачивание XLS файла")
    void xlsFileDownloadTest() throws IOException {
        open(DOWNLOAD_FILE_XLS);
        File xls = $$("a[href*='pricelist/cement").find(text("Цемент")).download();
        XLS parsedXls = new XLS(xls);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(12)
                .getCell(0)
                .getStringCellValue()
                .contains(XLS_RESULT);
        assertTrue(checkPassed);
    }
}
