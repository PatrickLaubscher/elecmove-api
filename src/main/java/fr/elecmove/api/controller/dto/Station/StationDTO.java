package fr.elecmove.api.controller.dto.Station;

public class StationDTO {

    private String id;
    private String name;
    private Double tarification;
    private String power;
    private String instruction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTarification() {
        return tarification;
    }

    public void setTarification(Double tarification) {
        this.tarification = tarification;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
