package com.tobiasandre.goestetica.database.model;

/**
 * Created by TobiasAndre on 04/08/2017.
 */

public class ResultItem {

    private int idResult;
    private String dsResult;
    private Double vlResult;

    public ResultItem(int idResult,String dsResult,Double vlResult){
        this.idResult = idResult;
        this.dsResult = dsResult;
        this.vlResult = vlResult;
    }

    public int getIdResult() {
        return idResult;
    }

    public void setIdResult(int idResult) {
        this.idResult = idResult;
    }

    public String getDsResult() {
        return dsResult;
    }

    public void setDsResult(String dsResult) {
        this.dsResult = dsResult;
    }

    public Double getVlResult() {
        return vlResult;
    }

    public void setVlResult(Double vlResult) {
        this.vlResult = vlResult;
    }
}
