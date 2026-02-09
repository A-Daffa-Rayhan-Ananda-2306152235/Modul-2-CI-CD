### Reflection 1
In this tutorial, I have implemented several clean code methods. Here are the following methods.
- **Descriptive Names**
    
    I used descriptive variable and function names.
- **Avoiding Redundant Comments**

    I avoided writing redundant comments. I tried to depend on how descriptive my codes are instead of using too many comments.
- **Multiple Git Branches**

    I used different branches to separate different features. These branches are meant to make my work more organized.
- **Useful Commits**

    I always tried to commit everytime I made a change. This is so I can keep the history of my work. 

By the time I finished Excercise 1, I have not implemented any secure code methods. Therefore, secure coding is one of the aspects I can improve for my work. I might add authorization and authentication methods for my web application in the future.

### Reflection 2
1. I feel safe and more confident with my codes after writing the unit tests. For now, my codes seem bug-free to me.
The number of unit tests actually depends on how big your project is. In this tutorial, we are only developing 
a small web project. Therefore, I only created a few unit tests. Ideally, you need to make at least one unit test for
each method you implement. I got 100% code coverage from unit tests I created. My codes passed all the unit tests, but
it does not mean my codes have no bugs or logic erros. One of the reasons is because I only created relatively small
unit tests. I still do not know what will happen if I have like 1000 products on the web.

2. Implementing the new functional task will be repetitive. In the `CreateProductFunctional.java`, we already made
a functional test to verify Create feature. If we have to make the new functional test, there will be a lot of 
identical lines to `CreateProductFunctional.java`. For example, clicking the create button, filling out the form,
submitting the form, and iterating the table. Imagine if we have to make other similar functional tests, count how 
many identical lines we have to copy-paste. It is bad for maintainability too. Imagine if we have to switch from 
`ChromeDriver` to a headless browser, count how many identical lines we have to change. One of the solutions to
this issue is to create a base class (Inheritance). We can create a `BaseFunctionalTest` class that contains all the
common configuration. All specific functional tests can `extend` this base class.
```java
public abstract class BaseFunctionalTest {
    @LocalServerPort
    protected int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    protected String testBaseUrl;

    protected String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }
}
```