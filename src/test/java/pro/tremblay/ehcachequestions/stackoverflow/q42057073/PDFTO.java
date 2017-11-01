package pro.tremblay.ehcachequestions.stackoverflow.q42057073;

import java.io.Serializable;

/**
 * @author Henri Tremblay
 */
public class PDFTO implements Serializable {
    public final String filename;
    public final byte[] content;

    public PDFTO(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
    }
}
