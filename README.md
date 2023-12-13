# jenkins-shared-library

This is a repository to host all the common patterns that arise during the CI/CD of roboshop components development.

```
This jenkins-shared-libary vars/ is going to host all the common stages and we are going to import this library inturn the functions to keep the code DRY.
```

Ref : https://www.jenkins.io/doc/book/pipeline/shared-libraries/


### Why Shared Libraries In Jenkins ?

```
    As Pipeline is adopted for more and more projects in an organization, common patterns are likely to emerge. Oftentimes it is useful to share parts of Pipelines between various projects to reduce redundancies and keep code "DRY" [1].

    Pipeline has support for creating "Shared Libraries" which can be defined in external source control repositories and loaded into existing Pipelines.

```

### Shared Library Structure

Directory structure
The directory structure of a Shared Library repository is as follows:

```
    (root)
    +- src                     # Groovy source files
    |   +- org
    |       +- foo
    |           +- Bar.groovy  # for org.foo.Bar class
    +- vars
    |   +- foo.groovy          # for global 'foo' variable
    |   +- foo.txt             # help for 'foo' variable
    +- resources               # resource files (external libraries only)
    |   +- org
    |       +- foo
    |           +- bar.json    # static helper data for org.foo.Bar
```

>>> The src directory should look like standard Java source directory structure. This directory is added to the classpath when executing Pipelines.

>>> The vars directory hosts script files that are exposed as a variable in Pipelines. The name of the file is the name of the variable in the Pipeline. So if you had a file called vars/log.groovy with a function like def info(message)…​ in it, you can access this function like log.info "hello world" in the Pipeline. You can put as many functions as you like inside this file. Read on below for more examples and options.