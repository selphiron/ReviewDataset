/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reviewdataset;

import java.util.List;

/**
 *
 * @author AlbertSanchez
 */
public class Errors {
    
    private int number;
    private List<Long> diference;
    
    public Errors(int number, List<Long> diference) {
        this.number = number;
        this.diference = diference;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Long> getDiference() {
        return diference;
    }

    public void setDiference(List<Long> diference) {
        this.diference = diference;
    }
    
}
