# SimpleSiteCrawler

The goal of task was to write a simple web crawler that locates a user-selected element on a web site with frequently changing information.
I needed to write a program that analyzes HTML and finds a specific element, even after changes, using a set of extracted attributes.
I had few samples, that can be founded in samples folder. The target element that needed to be found by my program is the green 
“Everything OK” button. Any user can easily find this button visually, even when the site changes. Original contains a button with 
attribute id="make-everything-ok-button". This id is the only exact criteria, to find the target element in the input file.

The program must consume the original page to collect all the required information about the target element. Then the program should 
be able to find this element in diff-case HTML document that differs a bit from the original page. Original and diff-case HTML 
documents should be provided to the program in each run - no persistence is required.

Results for the samples can be found in results folder, by number.

There is also jar file, that can be ran with run.bat file.
