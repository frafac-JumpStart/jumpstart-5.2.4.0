package jumpstart.util.query;

public enum ComparisonOperator {
    LT ("Less Than"),
    LE ("Less Than Or Equal To"),
    EQ ("Equal"),
    GE ("Greater Than Or Equal To"),
    GT ("Greater Than"),
    NE ("Not Equal");
    
    private String description;
    private ComparisonOperator(String description) {
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
