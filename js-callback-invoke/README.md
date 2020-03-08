# js-callback-invoke

This simple (though vaguely) useful Vaadin 14 application displays a grid and some filters of the unclaimed estates held by the UK Treasury Solicitor (https://www.gov.uk/government/statistical-data-sets/unclaimed-estates-list). 

It serves as a convenient starting point for creating examples and doing tests.

![Alt text](screenshot.png?raw=true "Example of the Application")

# Running the Application

Assuming you have done a Maven build of the application, you should be able to start it up with:

```
mvn jetty:run
```

The application should then be available at http://localhost:8080 
  

# The Javascript Interactivity

The Vaadin Java code can expose a function to Javascript with the appropriate annotation:

```
@ClientCallable
private void setPlaceOfBirthFromJS(String placeOfBirthParam)
{
  placeOfBirth.setValue(placeOfBirthParam);
}
```

This code, taken from the example updates the 'Place of Birth' filter on the application. It can be invoked from third-party libraries with code such as : 

```
document.getElementById("placeOfBirthCombo");
element.$server.setPlaceOfBirthFromJS("London");
```

For reference - have a look at : https://vaadin.com/forum/thread/17171083/how-do-i-get-javascript-callback-working  

