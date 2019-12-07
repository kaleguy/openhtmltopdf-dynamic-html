# Dynamic HTML to PDF

This project is an example of using the OpenHTMLtoPDF library to generate 
a PDF from dynamically created XHTML and SVG. 

The current content consists of a table generated from JSON data, and a 
HighCharts graph.

A Java class loads the page into a headless Chrome browser, extracts the 
generated XHTML, writes it to a temporary file, and then runs that file
through OpenHTMLToPDF. 

