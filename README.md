### Reflection

1. **Code Quality**

    In this module, I implemented SonarCloud as an analysis tool for continuous integration (CI). 
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