# Threads-JDBS, Grade 78.00 / 100.00
In this exercise, I build a simple web crawler that scans URLs of images, record thread performance,
and stores them in a database (only the URL, not the image itself).


Requirements for my exercise:
1.	Input: a TEXT file containing one URL per line. The program must check that the file exists (if it doesn’t it displays an error message and exits), the file may be empty in which case it will do nothing.
2.	The program receives 4 mandatory arguments: 
a.	first a pool size (positive non zero number, see below), 
b.	second a delay for retries (positive non zero milliseconds), 
c.	third a number of retries, 
d.	fourth a file name (1)
3.	If any argument is missing or invalid it displays a usage message and exits
4.	The program creates one thread per URL to be processed.
5.	The number of simultaneous threads is limited (the pool size) therefore you must handle a thread pool for threads processing the URLs (explained in class)
6.	The URL will be analyzed and only URL of images will be inserted in the database. If the image URL is already in the database, it should not be inserted (no duplicates).
7.	When connecting to a URL, in case of connection/read failure your thread should sleep() for the duration given as delay for retries (the 2nd argument given in the program), then try X times to reconnect (X being the 3rd argument given in the program)
8.	In the case the URL is simply malformed there is no point to try again (you give up), the retries should be used only on valid URLs (connection problems). 
Program Arguments
The main class of your program is named Crawl.
For example:
	java Crawl  5 1000 3 urls.txt
means the program will handle a pool size of 5 threads, the retry delay will be one second, the max number of retries will be 3 times and the file containing the urls will be named urls.txt.

Output:
The program records the performance of each thread: elapsed time (thread duration measured with System.currentTimeMillis()), and print out results in the order the input file was given (which is not necessarily the threads execution order):
http://a.b.c/logo.jpeg : 1002 ms
http://a.b.c/abc: 45 ms 
http://a.b.c/def : timeout
x.com/gh : failed

I noted the possible cases:
•	Success opening the URL, displaying the thread duration
•	Malformed URL: displaying “failed”
•	Tried and ran out of time: “timeout”

The program also displays the whole content of the database after all URL are entered (perform a “SELECT *” in the table after all threads are finished). 

The database:
The database named ex2 is defined as follow. I assume it already exists.
