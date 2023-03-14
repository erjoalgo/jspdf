import java.io.File;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;

public class AddJavaScript {

    static final String SAMPLE_PDF = "C-2__20191020.pdf";
    static final String SAMPLE_JS = "app.alert( {cMsg: 'this is an example', nIcon: 3,"
        + " nType: 0, cTitle: 'PDFBox Javascript example’} );";

    static String readTextFile(String pathString) throws java.io.IOException {
        Path path = Paths.get(pathString);
        byte[] bytes = Files.readAllBytes(path);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        return String.join("\n", lines);
    }

    static String maybeStripSuffix(String suffix, String s) {
        if (s.endsWith(suffix)) {
            return s.substring(0, s.length() - suffix.length());
        }
        return s;
    }

    public static void main(String args[]) throws Exception {

        String inputPDF = args.length >= 1? args[1] : SAMPLE_PDF;
        String javaScript = args.length >= 2? readTextFile(args[2]) : SAMPLE_JS;
        String outputPDF = maybeStripSuffix(".pdf", inputPDF) + "-injected.pdf";

        System.out.printf("Injecting %s bytes of javascript into %s and saving as %s\n",
                          javaScript.length(), inputPDF, outputPDF);

        File file = new File(inputPDF);
        PDDocument document = Loader.loadPDF(file);

        PDActionJavaScript PDAjavascript = new PDActionJavaScript(javaScript);
        document.getDocumentCatalog().setOpenAction(PDAjavascript);
        document.save(new File(outputPDF));
        document.close();

        System.out.printf("Saved PDF with injected javascript as %s\n", outputPDF);
    }
}
