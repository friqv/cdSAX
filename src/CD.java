public class CD {

    private String TITLE;

    private String ARTIST;

    private String COUNTRY;

    private String COMPANY;

    private int YEAR;
    private double PRICE;

    public int getYEAR() {
        return YEAR;
    }

    public void setYEAR(int YEAR) {
        this.YEAR = YEAR;
    }

    public double getPRICE() {
        return PRICE;
    }

    public void setPRICE(double PRICE) {
        this.PRICE = PRICE;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }
    @Override
    public String toString() {
        return String.format("TITLE: %s%nARTIST: %s%nCOUNTRY: %s%nCOMPANY: %s%nPRICE: %.2f%nYEAR: %d%n", TITLE, ARTIST, COUNTRY, COMPANY, PRICE, YEAR);
    }
}
