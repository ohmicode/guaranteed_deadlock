During one of Java interviews years ago I was asked to write some concurrent code with guaranteed deadlock.

While it is relatively easy to write some code with a race condition (which CAN end up with deadlock),
I found it quite challenging to make it ALWAYS provide the deadlock without any timer trick(s).

Here is my collection of different ways to get a 100% deadlock.
