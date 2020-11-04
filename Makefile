all:
	javac Guiao1/*.java
	javac Guiao2/*.java
	javac Guiao4/*.java

guiao1Ex1:
	time java Guiao1.Ex1

guiao1Ex2:
	time java Guiao1.Ex2

guiao1Ex3:
	time java Guiao1.Ex3

guiao2Ex2:
	time java Guiao2.BankTest

guiao2Ex3:
	time java Guiao2.BankTestEx3

guiao4Ex2:
	time java Guiao4.TestAgreement

clean:
	rm Guiao1/*.class
	rm Guiao2/*.class
	rm Guiao4/*.class