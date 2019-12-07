# Dynamic HTML to PDF

This project is an example of using the OpenHTMLtoPDF library to generate 
a PDF from dynamically created XHTML and SVG. 

The current content consists of a table generated from JSON data, and a 
HighCharts graph.

A Java class loads the page into a headless Chrome browser, extracts the 
generated XHTML, writes it to a temporary file, and then runs that file
through OpenHTMLToPDF. 

OpenHTMLToPDF supports a subset of the CSS Paged Media spec, and the current 
example demos pagination, footers and bookmarks.

To run the project, download chromedriver to the project folder, or edit 
the Java code to point to your local chromedriver.

Sample output is in out/out.pdf.

[Sample Output](https://github.com/kaleguy/openhtmltopdf-dynamic-html/blob/master/out/out.pdf)

License: MIT
