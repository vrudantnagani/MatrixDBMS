package com.example.matrixdbms;

public class MatrixDBMS {
    private String id;
    private String part;
    private String manufacturer;
    private String category;
    private String installation_date;
    private String modification_date;
    private String operator_name;

    public MatrixDBMS() {
    }

    public MatrixDBMS(String id, String part, String manufacturer, String category, String installation_date, String modification_date, String operator_name) {
        this.id = id;
        this.part = part;
        this.manufacturer = manufacturer;
        this.category = category;
        this.installation_date = installation_date;
        this.modification_date = modification_date;
        this.operator_name = operator_name;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstallation_date() {
        return installation_date;
    }

    public void setInstallation_date(String installation_date) {
        this.installation_date = installation_date;
    }

    public String getModification_date() {
        return modification_date;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }
}
