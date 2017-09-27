This repository contains code, exercises and solutions from the book "Functional Programming in Kotlin". Along with the book itself, it's the
closest you'll get to having your own private functional programming tutor without actually having one.

[![Build Status][badge-travis]][travis]

[badge-travis]: https://travis-ci.org/fpinkotlin/fpinkotlin.png?branch=master
[travis]: https://travis-ci.org/fpinkotlin/fpinkotlin

## How to use the project

The code is available as a Gradle project that may be used  by:

- Running Gradle commands in a terminal
- Importing in Eclipse (with limitations)
- Importing in IntelliJ

One may of course use only the source files to import them into any other editor and/or compile and run them from the command line. Running Gradle
commands will not be described here. If you choose this way, you should already know how to do it.

## Downloading the project

There are two ways to download this project. You may:

- Click on the **Download ZIP** button on this page (https://github.com/fpinjava/fpinjava) to download a ZIP archive, then unzip it in the directory
of your choice
- Or you can clone the project using Git, in order to be able to update it easily when modifications or additions are made to the project. For this,
you will need to have Git installed on your PC and use the URL available on this page in the **SSH clone URL** area.

## Importing into Eclipse

To be done

### Importing the project

You now need to import the `fpinkotlin-parent` project into Eclipse:

To be done

## Importing into IntelliJ

To be done

## Doing the exercises

For each chapter, you will find two modules called `chaptername-exercises` and `chaptername-solutions` . Go to the first exercise in
the `src/main/kotlin` hierarchy. Here, you will find some code with either a comment saying "To be implemented" or method(s) with the
implementation replaced with a single line throwing a runtime exception. Just implement the missing code.

Note that code is often duplicated from one exercise to the another, so you should not look at the code for exercise 2 before doing exercise 1,
since exercise 2 will often contain the solution to exercise one.

## Verifying your answers

To verify that your solution is working, go to the corresponding unit test, in the `src/test/kotlin` hierarchy of the same module. Right-click
on the test class (which has the same name as the exercise class with the addition of the `Test` suffix) and select **Run**. The test should
succeed. If it doesn't, fix your code and try again.

## Looking at solutions

If you don't find the correct solution to an exercise, you can look at the corresponding `chaptername-solutions` module. You may run the solution
test to verify that the solution is working.

## Remarks

Lots of code is duplicated. This is done so that all exercises are made as independent as possible. However, code reused from previous chapters
is copied to the `fpinjava-common` module and should be used from there.

## Module names

Code modules are generally named after the chapter titles, and not the chapter numbers, which sometimes make them difficult to find. Here is the list of the modules:

* Chapter 1: fpinkotlin-introduction

* Chapter 2: This chapter has no corresponding module

* Chapter 3: fpinkotlin-functions

* Chapter 4: fpinkotlin-recursion

* Chapter 5: fpinkotlin-lists

Most modules exist in two versions: exercises and solutions. However, chapters 1 and 2 have no exercises.





