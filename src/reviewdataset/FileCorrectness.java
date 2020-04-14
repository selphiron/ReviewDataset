/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reviewdataset;

/**
 *
 * @author AlbertSanchez
 */
public class FileCorrectness {
    
    private String namefile;
    private boolean okFile;
    private boolean okFormat;
    private Errors errors;

    public String getNamefile() {
        return namefile;
    }

    public void setNamefile(String namefile) {
        this.namefile = namefile;
    }

    public boolean isOkFile() {
        return okFile;
    }

    public void setOkFile(boolean okFile) {
        this.okFile = okFile;
    }

    public boolean isOkFormat() {
        return okFormat;
    }

    public void setOkFormat(boolean okFormat) {
        this.okFormat = okFormat;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
    
}
