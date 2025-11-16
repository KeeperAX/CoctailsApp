package models;

public class PreparationStep {
    private int stepNumber;
    private String description;
    private String tips; // советы по шагу
    private int duration; // в секундах

    public PreparationStep(int stepNumber, String description, String tips, int duration) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.tips = tips;
        this.duration = duration;
    }

    // Getters and Setters
    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Шаг " + stepNumber + ": " + description;
    }
}
