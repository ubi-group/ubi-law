package com.itcag.legalyzer.eval;

public class Stats {

    private final String model;
    
    private final long timestamp = System.currentTimeMillis();
    
    private Double accuracy = null;
    private Double precision = null;
    private Double recall = null;
    private Double f1 = null;
    
    private Integer tp = null;
    private Integer fp = null;
    private Integer tn = null;
    private Integer fn = null;
    
    private Double auc = null;
    private Double auprc = null;
    
    public Stats(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public Double getRecall() {
        return recall;
    }

    public void setRecall(Double recall) {
        this.recall = recall;
    }

    public Double getF1() {
        return f1;
    }

    public void setF1(Double f1) {
        this.f1 = f1;
    }

    public Integer getTp() {
        return tp;
    }

    public void setTp(Integer tp) {
        this.tp = tp;
    }

    public Integer getFp() {
        return fp;
    }

    public void setFp(Integer fp) {
        this.fp = fp;
    }

    public Integer getTn() {
        return tn;
    }

    public void setTn(Integer tn) {
        this.tn = tn;
    }

    public Integer getFn() {
        return fn;
    }

    public void setFn(Integer fn) {
        this.fn = fn;
    }

    public Double getAuc() {
        return auc;
    }

    public void setAuc(Double auc) {
        this.auc = auc;
    }

    public Double getAuprc() {
        return auprc;
    }

    public void setAuprc(Double auprc) {
        this.auprc = auprc;
    }
    
}
