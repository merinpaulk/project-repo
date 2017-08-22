package enums;


public abstract class P3SAbstractEnum {

    protected String value;

    
    public void fail(String message) {
    	System.out.println("P3SAbstractEnum in fail() !!! ");
    }
    
    public String toString() {
                return this.value;
    }

}
