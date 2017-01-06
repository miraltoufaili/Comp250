
import java.math.BigInteger;
import java.util.Random;

public class Tester {

	static String s1;
	static String s2;
	static int base;

	static Random rn = new Random();

	static BigInteger big1;
	static BigInteger big2;
	static NaturalNumber n1;
	static NaturalNumber n2;
	
	public static void main(String[] args) throws Exception {

		// preliminary tests
		preliminaryTest();

		// verify that the inputs are not modified
		modifiedInputsTests();

		// test carry in add()
		carryTest();

		// edge cases (different length, big - 1)
		edgeCases();

		// test running time with very large number
		largeNumbers();

		// test running time of large divided by small
		slowDivide();

		System.out.println("All done :)");

	}

	private static void slowDivide()
	{
		System.out.println("Testing slow divide...");
		s1 = "756451284928263112589262932124789";
		s2 = "2";
		base = 10;
		initializeNumbers();
		testDivide();
		System.out.println("... done!");
	}

	private static void preliminaryTest()
	{
		randomTests(5,5,10);
		System.out.println("Finished preliminary tests!");
	}

	// perform ntests tests with randomly generated numbers of length l1 and l2
	private static void randomTests(int l1, int l2, int ntests)
	{
		// try some random inputs
		for (int i = 0; i < ntests; i++) {
			generateStringBase(l1, l2);
			initializeNumbers();
			//System.out.println("... plus");
			testPlus();
			//System.out.println("... minus");
			testMinus();
			//System.out.println("... times");
			testTimes();
			//System.out.println("... divide");
			testDivide();
		}
	}

	private static void carryTest()
	{
		s1 = "9";
		s2 = "1";
		base = 10;
		carryHelper();

		s1 = "7";
		s2 = "1";
		base = 8;
		carryHelper();

		System.out.println("Carry in add works fine!");
	}

	private static void edgeCases()
	{
		s1 = "1000000000";
		s2 = "1";

		for (int base = 2; base < 11; base++ ) {
			initializeNumbers();
			testMinus();
		}

		randomTests(14,7,5);
		randomTests(10,5,5);


		System.out.println("Edge cases passed!");
	}

	private static void largeNumbers()
	{
		System.out.println("... starting tests with large numbers (should take just a few seconds)");
		randomTests(500,500,1);
		System.out.println("Finished large numbers!");
	}

	private static void modifiedInputsTests()
	{
		s2 = "1234";
		s1 = "9876";
		base = 10;

		initializeNumbers();
		NaturalNumber clone1 = n1.clone();
		NaturalNumber clone2 = n2.clone();
		NaturalNumber dummy;
		try {
			dummy = n1.plus(n2);
		}
		catch (Exception e) {

		}
		if (!clone1.toString().equals(n1.toString()) || !clone2.toString().equals(n2.toString()))
			System.out.println("    add() method modifies an input.");

		initializeNumbers();
		clone1 = n1.clone();
		clone2 = n2.clone();
		try {
			dummy = n1.minus(n2);
		}
		catch (Exception e) {}
		if (!clone1.toString().equals(n1.toString()) || !clone2.toString().equals(n2.toString()))
			System.out.println("    minus() method modifies an input.");
		
		initializeNumbers();
		clone1 = n1.clone();
		clone2 = n2.clone();
		try {
			dummy = n1.times(n2);
		}
		catch (Exception e) {}
		if (!clone1.toString().equals(n1.toString()) || !clone2.toString().equals(n2.toString()))
			System.out.println("    times() method modifies an input.");	

		initializeNumbers();
		clone1 = n1.clone();
		clone2 = n2.clone();
		try {
			dummy = n1.divide(n2);
		}
		catch (Exception e) {}
		if (!clone1.toString().equals(n1.toString()) || !clone2.toString().equals(n2.toString()))
			System.out.println("    divide() method modifies an input.");	

		System.out.println("Finished testing for modified inputs!");
	}

	private static void testPlus()
	{
		try {
			if (!("("+big1.add(big2).toString(base)+")_"+base).equals(n1.plus(n2).toString()))
				System.out.println("    Wrong answer:  " + n1 + ".plus(" + n2 + ")" + " should be ("+big1.add(big2).toString(base)+")_"+base + " but is " + n1.plus(n2).toString());
		}
		catch (Exception e){
			System.out.println("    Exception with " + n1 + ".plus(" + n2 + "): " + e.getMessage());
		}
	}

	private static void testMinus()
	{
		NaturalNumber n1t = n1;
		NaturalNumber n2t = n2;
		BigInteger big1t = big1;
		BigInteger big2t = big2;
		if (big1.compareTo(big2) < 0)
		{
			n1t=n2;
			n2t=n1;
			big1t=big2;
			big2t=big1; 
		}
		try {
			if (!("("+big1t.subtract(big2t).toString(base)+")_"+base).equals( n1t.minus(n2t).toString()))
				System.out.println("    Wrong answer: " + n1t + ".minus(" + n2t + ")" + " should be (" + big1t.subtract(big2t).toString(base)+")_"+base + " but is " + n1t.minus(n2t).toString());
		}
		catch (Exception e){
			System.out.println("    Exception with " + n1t + ".minus(" + n2t + "): "+ e.getMessage());
		}
	}

	private static void testTimes()
	{
		try {
			if (!("("+big1.multiply(big2).toString(base)+")_"+base).equals(n1.times(n2).toString()))
				System.out.println("    Wrong answer: "+ n1 + ".times(" + n2 + ")" + " should be " + "("+big1.multiply(big2).toString(base)+")_"+base + " but is " + n1.times(n2).toString());
		}
		catch (Exception e){
			System.out.println("    Exception with: " + n1 + ".times(" + n2 + "): "+ e.getMessage());
		}
	}

	private static void testDivide()
	{
		try {
			String res1 = "("+big1.divide(big2).toString(base)+")_"+base;
			String res2 = n1.divide(n2).toString();
			if (!((res1.contains("(0)") && res2.contains("()")) || res1.equals(res2)))
				System.out.println("    Wrong answer: " + n1 + ".divide(" + n2 + ") should be" + res1 + " but is " + res2);
		}
		catch (Exception e){
			System.out.println("    Exception with " + n1 + ".divide(" + n2 + "): " + e.getMessage());
		}
	}

	private static void generateStringBase(int lengthA, int lengthB)
	{
		s1 = "";
		s2 = "";
		base =  rn.nextInt(9) + 2;
		s1 += (rn.nextInt(base-1) + 1);
		s2 += (rn.nextInt(base-1) + 1);
		for (int i = 0; i < lengthA-1; i++)
		{
			s1 += rn.nextInt(base-1);
		}
		for (int i = 0; i < lengthB-1; i++)
		{
			s2 += rn.nextInt(base-1);
		}
	}

	private static void initializeNumbers()
	{
		big1 = new BigInteger(s1,base);
		big2 = new BigInteger(s2,base);
		try {
			n1 = new NaturalNumber(s1, base);
			n2 = new NaturalNumber(s2, base);
		}
		catch (Exception e)
		{
		}
	}

	private static void carryHelper()
	{
		initializeNumbers();
		try {
			if (!n1.plus(n2).toString().equals("(10)_" + base))
				System.out.println("    Problems with carry in add");
		}
		catch (Exception e)
		{
			System.out.println("    Exception when add with carry: " + e.getMessage());
		}
	}
	
}
