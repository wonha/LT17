@title[Introduction]
__Functional style programming__<br />
__(λ style programming)__

---
@title[Agenda]

- Purely λ programming
- λ style programming |
- Multitasking |
    - Concurrency & Parallelism
    - Synchronous & Asynchronous
    - Blocking & Non-blocking |
- λ style programming with Multitasking |

---
@title[Purely λ programming]

- Higher-order function |
- First-class function

+++
@title[Sample for First-class function]

```haskell
applyTwice :: (a -> a) -> a -> a
applyTwie f x = f (f x)
```
@[2](Takes function as an argument)

- Didn't validate yet
```
ghci> applyTwice (+10) 10
30
ghci> applyTwice ("f(" ++) "x" applyTwice (++ ")")
f(f(x))
```
@[1-2]
@[3-4]

+++
@title[Purely λ programming 2]

- Higher-order function
- First-class function
- Closure |

+++
@title[Sample for Closure]

```haskell
f a b c = a * b + c
f a b = \ c -> a * b + c
```
@[2](f returns closure)

---
@title[λ style programming]

- Closure
- Anonymous function
- Anonymous Class
- Lambda

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

