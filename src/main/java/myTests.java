public class myTests {
    public void myMethod() {

        int [] myMethod = new int[10];

        int [] myInts = {1,2,3};

        String [] myStrings = {"shahin", "ali", "behrooz" };
        for (String i: myStrings) {
            System.out.printf(" %s\n", i);
        }

    }

    public static void main(String[] args) {
        myTests mytests = new myTests();
        mytests.myMethod();
    }
}
