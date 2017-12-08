@title[Introduction]
__Functional style programming__<br />
__(λ style programming)__

---
See this slides from [online repo](https://gitpitch.com/wonha/LT17/master?p=functional_style/) !
![Octocat](https://d1z75bzl1vljy2.cloudfront.net/kitchen-sink/octocat-daftpunkocat.gif)

---
@title[Agenda]

- Purely λ programming
- Purely λ & λ style VS Imperative style |
- λ style programming |
- Multitasking |
    - Concurrency & Parallelism
    - Synchronous & Asynchronous
    - Blocking & Non-blocking
- λ style programming with Multitasking |
    - Pipelining
    - Concurrency & Parallelism
    - Synchronous & Asynchronous
    - Blocking & Non-blocking

---
@title[Purely λ programming]

__Purely λ programming__

- First-class function |
<!-- the language treats function as value(variable) -->
<!-- not all the languages that support first class function is purely functional programming language e.g. JavaScript, Lisp-->
- Higher-order function |
<!-- function that work on other functions (Either takes function as an argument or return function -->

+++
@title[Sample for Higher-order function]

__Higher-order function with Haskell__
```haskell
applyTwice :: (a -> a) -> a -> a
applyTwie f x = f (f x)
```
@[1](Define Higher-order function 'applyTwice')
@[1]('applyTwice' takes a first-order function as an argument)
@[2]('applyTwice' is a function that works on function 'f')

+++
@title[Sample for Higher-order function 2]

<!--Didn't validate yet -->
__Execution of 'applyTwice'__
```
ghci> applyTwice (+10) 10
30
ghci> applyTwice ("f(" ++) "x" applyTwice (++ ")")
f(f(x))
```
@[1]
@[1-2]
@[3]
@[3-4]

+++
@title[Purely λ programming 2]

__Purely λ programming__

- First-class function
- Higher-order function
- Closure |
    - Free variable |
<!-- lexical scoped variable, global variable -->
<!-- research modifying policy of free variable in Perl, Python, Go, Java(Lambda) -->
<!-- In Haskell, free variable is actually a constant, there is no 'variable' in Haskell -->
<!-- In Perl, all the closures has copy of free variable's value within itself -->
<!-- In Java, all the closures(lambdas) shares one free variable, so modifying that free variable is restricted. Only effective final varibale can be used
<!-- In Python, -->
<!-- In Go, -->

+++
@title[Sample for Closure]

<!-- see the java sample for this from Java8 in action of ch11~14, that making function that takes 3 arg into function that takes 2 arg and return a function  -->
__Closure with Haskell__
```haskell
f a b c = a * b + c
f a b = \ c -> a * b + c
```
@[2]('f' returns closure)
@[2](both 'a' and 'b' are free variable)

+++
@title[Purely λ programming 3]

__Purely λ programming__

- First-class function
- Higher-order function
- Closure
    - Free variable
- Side-effect free function |
    - No mutable data structure |
    - No exception (error?) |
    - No I/O |
<!-- A function having side effect can be unpredictable depending on the state of the system-->
<!-- we can expect that side-effect free function returns the same value anytime, with any kind of situation -->
<!-- Java allows side effect in lambda, since we can add/remove values of free variable(Collection) -->

+++
@title[Purely λ programming 4]

__Purely λ programming__

- First-class function
- Higher-order function
- Closure
    - Free variable
- Side-effect free function
    - No mutable data structure
    - No exception (error?)
    - No I/O
- Referential transparency |
<!-- Research definition of REferential transparency -->
<!-- Java method that returns List (List<Customer> findAll(String id))-->
<!-- Tail call-->

+++
@title[Topics about FP]

There are lots of [other topics](https://en.wikipedia.org/wiki/List_of_functional_programming_topics)

---
@title[Implementation comparison]
__(purely) λ language VS λ style VS imperative style__

+++
@title[Quick sort logic comparison]
__Quick sort logic comparison__<br/>
@fa[arrow-down]

+++?code=./functional_style/assets/qsort.py&lang=python&title=Imperative style with Python
@[](Variables to persist program status, and flow control statement depends on these variables)

+++?code=./functional_style/assets/qsort.hs&lang=haskell&title=Purely λ with Haskell
@[](Declaration and Recursion instead of variable and flow control)

+++?code=./functional_style/assets/qsort.scala&lang=scala&title=λ style with Scala
@[](Declaration without low control, but varables exist)

+++?code=./functional_style/assets/qsort.kt&lang=kotlin&title=λ style with Kotlin

+++?code=./functional_style/assets/qsort.pl&lang=perl&title=λ style with Perl

+++?code=./functional_style/assets/qsort2.py&lang=perl&title=λ style with Python

+++?code=./functional_style/assets/qsort.lua&lang=lua&title=Imperative style with Coffee
+++?code=./functional_style/assets/qsort.lua&lang=lua&title=Imperative style with Lua
+++?code=./functional_style/assets/qsort.curry&lang=curry&title=Purely λ with Curry
+++?code=./functional_style/assets/qsort.hs&lang=haskell&title=Purely λ with Haskell
+++?code=./functional_style/assets/qsort.prolog&lang=prolog&title=Purely λ with Prolog
+++?code=./functional_style/assets/qsort.apl&lang=apl&title=Purely λ with APL
@[]([http://tryapl.org](http://tryapl.org/)

---
@title[λ style programming]
__λ style programming__

+++
@title[λ style programming]

- Closure
    - free variable (mutable data)

+++
@title[Sample for Closure in λ style]

```perl
sub make_counter {
    my ($start) = @_;
    return sub { $start++ }
}

my $from_ten = make_counter(10);
my $from_three = make_counter(3);

print $from_ten->();    # 10
print $from_three->();  # 3
print $from_ten->();    # 11
print $from_three->();  # 4
```
@[2](Variable '$start' is the free variable)
@[3](This function \(subroutine\) is the closure)
@[6-7](Free variavble '$start' is created 2 times)
@[9-12]

+++
@title[λ style programming 2]

- Closure
- Anonymous function |
- Anonymous class |
- Lambda |

+++
@title[]

__Declaration of Unix command__
```bash
$ cat hello.md
Welcome to LT17
Annyonghaseyo
End of year party !
$ cat hello.md | tr "[A-Z]" "[a-z]" | sort | head -2
annyonghaseyo
end of year party !
```
@[1-4]
@[5]
@[5-7]


---
@title[Multitasking]

+++
@title[Concurrency & Parallelism]
data pallelism / task
jenkov
google image

+++
@title[Synchronous & Asynchronous]
Servlet figure

+++
@title[Blocking & Non-blocking]
google image

+++?code=./functional_style/assets/ReactiveStreamsCallbackHell.java&lang=java&title=Callback hell 1
@[](Without declarative style of λ programming, callback hell is unavoidable)

+++
@title[Callback hell 2]
[Callbackhell.com](callbackhell.com)

+++
@title[Solution for callback hell]

Callbackhell with Reactive programming<br/>
Functional Reactive Programming (FRP) !

---
@title[λ style programming with Multitasking]

- Declarative way |
- Reduce Side effeuct |

+++
@title[Concurrency & Parallelism]
Reactor/Spring sample
Concurrent Unix command

+++
@title[Synchronous & Asynchronous]
Future sample

+++
@title[Blocking & Non-blocking]
Reactor/Spring sample
