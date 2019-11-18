//Muhammad Aamir Iqbal
/***
* Example JUnit testing class for Circle1 (and Circle)
*
* - must have your classpath set to include the JUnit jarfiles
* - to run the test do:
*     java org.junit.runner.JUnitCore Circle1Test
* - note that the commented out main is another way to run tests
* - note that normally you would not have print statements in
*   a JUnit testing class; they are here just so you see what is
*   happening. You should not have them in your test cases.
***/

import org.junit.*;

public class Circle1Test
{
   // Data you need for each test case
   private Circle1 newCircle;
   private Circle1 circle1;

// 
// Stuff you want to do before each test case
//
@Before
public void setup()
{
   System.out.println("\nTest starting...");
   circle1 = new Circle1(1,2,3);
}

//
// Stuff you want to do after each test case
//
@After
public void teardown()
{
   System.out.println("\nTest finished.");
}

//
// Test a simple positive move
//
@Test
public void simpleMove()
{
   Point p;
   System.out.println("Running test simpleMove.");
   p = circle1.moveBy(1,1);
   Assert.assertTrue(p.x == 2 && p.y == 3);
}

// 
// Test a simple negative move
//
@Test
public void simpleMoveNeg()
{
   Point p;
   System.out.println("Running test simpleMoveNeg.");
   p = circle1.moveBy(-1,-1);
   Assert.assertTrue(p.x == 0 && p.y == 1);
}
/************************************************************
//Tests Added
*************************************************************/

//Scale up Test

@Test
public void scaleUpTest(){
   System.out.println("Running Scale Up Test.");
   double newFactor = circle1.scale(3);
   Assert.assertTrue( newFactor == 9 );
}

//Scale down Test

@Test
public void scaleDownTest(){
   System.out.println("Running Scale Down Test.");
   double newFactor = circle1.scale(0.5);
   Assert.assertTrue( newFactor == 1.50 );
}

//Intersection Test

@Test
public void intersectionTest(){
   System.out.println("Running intersection Test.");
   newCircle = new Circle1(3, 4, 5);
   Assert.assertTrue(circle1.intersects(newCircle));
}

//Outer circles Test

@Test
public void outerCirclesTest(){
   System.out.println("Running Outer circles Test.");
   newCircle = new Circle1(16, 16, 4);
   Assert.assertFalse(circle1.intersects(newCircle));
}
 
//Inner Circle Test

@Test
public void innerCirlceTest()
{
   System.out.println("Running Inner Circle Test.");
   newCircle = new Circle1(1, 3, 3);
   Assert.assertFalse(circle1.intersects(newCircle));
}

//Intersection Test at certain points

@Test
public void intersectTest(){
   System.out.println("Running test Intersection Test at certain points.");
   newCircle = new Circle1(4, 3, 4);
   Assert.assertTrue(circle1.intersects(newCircle));
}






/*** NOT USED
public static void main(String args[])
{
   try {
      org.junit.runner.JUnitCore.runClasses(
               java.lang.Class.forName("Circle1Test"));
   } catch (Exception e) {
      System.out.println("Exception: " + e);
   }
}
***/

}

