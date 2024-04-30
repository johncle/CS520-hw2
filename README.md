# Automated Tool of Choice: EvoSuite

### [EvoSuite Video Walkthrough](https://youtu.be/BhmtQcIu6pk)

EvoSuite is an automated test suite generation tool for Java. It takes in Java 8 (or 9) classes and/or packages as bytecode, analyzes them, and generates JUnit 4 tests for the following test criteria:

-   Line Coverage
-   Branch Coverage
-   Exception
-   Mutation testing (weak)
-   Method-Output Coverage
-   Top-Level Method Coverage
-   No-Exception Top-Level Method Coverage
-   Context Branch Coverage

It works by using a evolutionary search algorithm called _Dynamic Many-Objective Sorting Algorithm_ (DynaMOSA) to analyze the Java bytecode and generate test cases that aim to maximize code coverage [[1]](#1). After generating the tests, it also works to improve user experience and code readability by minimizing the test suite and checking the tests for compile errors or flakiness if the target class uses non-deterministic methods.

It outputs two folders, evosuite-report and evosuite-tests:

evosuite-report contains a statistics.csv file containing the following statistics for each class:

-   `TARGET_CLASS` - The given Java class
-   `criterion` - Test criteria as shown above
-   `Coverage` - Percentage of code coverage by tests
-   `Total_Goals` - Number of total goals to test
-   `Covered_Goals` - Number of actual goals tested, since some tests are difficult to create such as mutation testing

evosuite-tests contains two .java files, one for scaffolding the Java framework that EvoSuite runs on, and one for the actual JUnit tests.

## Running EvoSuite:

1. In this guide we will be using EvoSuite-1.2.0 with Java JDK 8, Apache Maven 3.6.3, and JUnit 4.12 on Linux, but Java JDK versions 8 or 9, Maven >= 3.1, JUnit 4.\*, and Windows and MacOS should also work

    - [EvoSuite files](https://github.com/EvoSuite/evosuite/releases/tag/v1.2.0) are `evosuite-1.2.0.jar` (main executable) and `evosuite-standalone-runtime-1.2.0.jar` (runtime test dependency)
    - Maven should be given a dependency for JUnit 4.12. Alternatively if you don’t want to use Maven, you can provide junit.jar and hamcrest-core.jar and compile everything yourself
    - Check versions with `java -version`, `javac -version`, and `mvn -version`
    - See our [Example files](https://github.com/johncle/CS520-hw2)

2. In the parent directory, compile the .java code into .class files with `mvn compile`

    - This will create a `target/` folder containing the classes and Maven files

3. Run EvoSuite on the target classes with `java -jar evosuite-1.2.0.jar <target> [options]`. This produces the evosuite folders and test .java files

    - In the example we use `java -jar evosuite-1.2.0.jar -class Evo -projectCP target/classes`
    - `<target>` can be:
        - `-class <ClassName>` - A Java class name such as com.foo.myClass
        - `-prefix <PrefixName>` - Operates on all .class files with given prefix
        - `-target <jar file or directory>` - .jar or multiple classes
    - `-projectCP <ClassPath>` specifies where it should find the Java .class files
    - See `evosuite-1.2.0.jar -help` and [Commandline | EvoSuite](https://www.evosuite.org/documentation/commandline/) for more information

4. Set up the test environment with `mvn dependency:copy-dependencies`, which installs JUnit (and Hamcrest). Specify the dependencies to the Java compiler (javac) in CLASSPATH with `export CLASSPATH=target/classes:evosuite-standalone-runtime-1.2.0.jar:evosuite-tests:target/dependency/junit-4.12.jar:target/dependency/hamcrest-core-1.3.jar`

    - Replace colons (:) with semicolons (;) if using Windows

5. Compile JUnit tests with `javac evosuite-tests/*.java`

    - If your classes are in packages, make sure to specify subdirectories in evosuite-tests as well: `evosuite-tests/<subdirectories>/*.java`

6. Run JUnit tests with `java org.junit.runner.JUnitCore <ClassName>_ESTest`

    - In the example we use `java org.junit.runner.JUnitCore Evo_ESTest`

    Your output should look something like:

    ```
    $ java org.junit.runner.JUnitCore Evo_ESTest
    JUnit version 4.12
    ................................
    Time: 0.594

    OK (32 tests)
    ```

It works very well! It generates readable test cases to cover ~85\% of code for a variety of odd inputs such as null arrays, empty arrays, 0, negative numbers, and inputs that would otherwise break functionality. It also tests that functions work properly with normal inputs.

The tests typically handle exceptions well and provide clear comments to explain them. We created a method `sumOutOfBounds()` that would call `nums[nums.length]` to cause an index out of bounds error, and EvoSuite properly tested it for `ArrayIndexOutOfBoundsException`. However, some tests have a comment saying “Undeclared exception!” but it doesn’t catch or assert them in the test.

It was also quite clever in using methods together in test cases. We had methods to generate a range of integers similar to Python’s `range()`, and it used them to generate an integer array to test the sum methods with.

However, it’s not perfect. It seems to take the target code as it is and creates valid test cases for behavior that is unclear to be buggy or intended. For example, when testing the sum of a very large array that causes an integer overflow, it does `assertEquals((-367934752), int0)`. We also created a method `sumOffByOne()` that calculates the sum of a list but excluding its first element, testing if it would be able to tell what the code was trying to do (compute the sum of the entire list). This might be too opinionated for the tool so the behavior makes sense.

Pros:

-   Once the prerequisites are set up (Java 8, Maven 3.6.3, JUnit 4.12, installing the right EvoSuite version), EvoSuite is very easy to work with. You simply run the evosuite.jar file on the target Java classes and it just works
-   Readability is generally good and test cases have comments to explain them
-   Test quality is good for catching exceptions and edge cases

Cons:

-   Analyzes code at face value to generate tests for code coverage, but the tests may not be what developers are looking for like missed logic bugs, for example accidentally adding instead of subtracting
-   Even with a focus on readability, the test cases aren’t always clear on what specific part is being tested
-   Its last update was in 2021 which added Java 9 support, but our machine uses Java 21 so we had to install an older version. We decided on Java 8 because that is the latest LTS version before Java 9, but premier support for it already ended in March 2022

EvoSuite excels at creating first test cases with high code coverage for Java projects of any size, mainly being helpful for finding edge cases and exceptions. However, there would still need to be some human interaction to vet the test cases as some of them are unclear and it might not achieve 100% code coverage (if that is desired). It likely won’t be able to reveal crucial logic bugs to developers that may come from typos or oversight.

## References

<a id="1">[1]</a>
S. Vogl, S. Schweikl, G. Fraser, A. Arcuri, J. Campos and A. Panichella, "Evosuite at the SBST 2021 Tool Competition," 2021 IEEE/ACM 14th International Workshop on Search-Based Software Testing (SBST), Madrid, Spain, 2021, pp. 28-29, doi: 10.1109/SBST52555.2021.00012.
Abstract: EvoSuite is a search-based unit test generation tool for Java. This paper summarises the results and experiences of EvoSuite's participation at the ninth unit testing competition at SBST 2021, where EvoSuite achieved the highest overall score.
keywords: {Software testing;Java;Conferences;Tools;Test pattern generators}, URL: https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=9476230&isnumber=9476163, https://www.evosuite.org/publications/
