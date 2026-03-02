### Module 2 Reflection

1. **Code Quality**

    In module 2, I implemented SonarCloud as an analysis tool for continuous integration (CI). 
    I encountered several code quality issues that I later fixed:
     - **Low code coverage**: Even though I have made some unit tests for my code, JaCoCo detected some 
    lines of code that have not been tested. In order to fix this, I created more unit tests until I reached
    100% code coverage.
     - **Bad code**: I failed SonarCloud CI several times because of its strict rules on how to write good code.
     Even though it is annoying at first, I ultimately learnt that my code must satisfy the standards. 
     The standards are like import order and maximum number characters each line. In order to fix this, I rewrite
     my code to satisfy all standards.

2. **CI/CD Workflows**
    
    CI/CD workflows I implemented are quite simple. I used SonarCloud and Koyeb for CI and CD. In my opinion,
    the current implementation has met the definition of CI and CD. Every time I push something this GitHub
    repository, SonarCloud analysis will check whether my code passes all standards or not. After that, Koyeb
    will automatically redeploy the web from this GitHub repository project.

### Module 3 Reflection

1. **SOLID Principles**

    In module 2, I have implemented SOLID Principles:
    - **Single Responsibility Principle (SRP):** I separated Product and Car tasks. Product components have to do 
    their own tasks, so do Car components. In order to make it happen, I remove Car's inheritance from Product.
    - **Open-Closed Principle (OCP):** I made Product and Car classes more similar. So, it will be easier to
   generalize and extend both classes.
    - **Liskov Substitution Principle (LSP):** I removed Car's inheritance from Product, because both classes
   do different tasks. Product can not be replaced by Car, vice versa.
    - **Interface Segregation Principle (ISP):** I made each client only implement a relevant interface.
    - **Dependency Inversion Principle (DIP):** I changed `@Autowired private CarServiceImpl carService;` to `@Autowired private CarService carService;`
   in `CarController.java` from branch `before-solid`. So, all classes only depend upon interfaces or classes.

2. **Advantages of SOLID Principles**

    By applying SOLID principles, we can make the codebase more:
    - **Maintainable:** It is easier to modify.
    - **Reusable:** It is easier to reuse.
    - **Extendable:** We do not to change the code too much if we add new features.
    - **Readable:** It is cleaner.
    - **Testable:** It can reduce the number of unit tests needed.

3. **Disadvantages of not Applying SOLID Principles**
    
    If we do not apply SOLID principles, the codebase will be more:
    - **Fragile:** It is easier to encounter unwanted bugs.
    - **Not reusable:** It is harder to reuse.
    - **Rigid:** We have to change the codebase a lot if we add new features.
    - **Redundant:** There will be a lot of clients that implement unrelevant tasks.
    - **Not Testable:** We have to create a lot of unit tests.