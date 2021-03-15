package ie.ait.tavares.pogo.application;

public enum League {

    GREAT(1500),
    ULTRA(2500),
    MASTER(10000);

    private int maxCp;

    League(int maxCp) {
        this.maxCp = maxCp;
    }

    public int getMaxCp() {
        return maxCp;
    }
}
